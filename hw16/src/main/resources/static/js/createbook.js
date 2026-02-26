window.addEventListener('load', () => {
    getAuthors();
    getGenres();
    getBook();
    });

function getAuthors(){
    const createBookChooseAuthor = document.getElementById("create-book-choose-author").textContent;
    const path=document.getElementById('data-rest-base-path').textContent;

    fetch(`${path}authors`)
        .then(response => {
            let errorText;
            if (response.ok) {
              return response.json();
            }else if (response.status === 403) {
                errorText = document.getElementById('connection-forbidden-error-text').textContent;
            }else if(response.status === 404){
                return [];
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
            }
            const selectAuthors = document.getElementById("select-authors");
            selectAuthors.innerHTML =`<option value="" disabled selected hidden>${errorText}</option>`;
        })
        .then(data => {
            const authors = data._embedded?.authors || [];
            const selectAuthors = document.getElementById("select-authors");
            selectAuthors.innerHTML = `
                <option value="" disabled selected hidden>${createBookChooseAuthor}</option>
                ${authors.map(author => `
                      <option value="${author._links.self.href}">${author.fullName}</option>
                    `).join('')}`;
        });
}

function getGenres(){
    const path=document.getElementById('data-rest-base-path').textContent;

    fetch(`${path}genres`)
        .then(response => {
            let errorText;
            if (response.ok) {
              return response.json();
            }else if (response.status === 403) {
                errorText = document.getElementById('connection-forbidden-error-text').textContent;
            }else if(response.status === 404){
                return [];
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
            }
            const selectGenres = document.getElementById("select-genres");
            selectGenres.innerHTML =`<option value="" disabled>${errorText}</option>`;
        })
        .then(data => {
            const genres = data._embedded?.genres || [];
            const selectGenres = document.getElementById("select-genres");
                selectGenres.innerHTML = genres.map(genre => `
                  <option value="${genre._links.self.href}">${genre.name}</option>
                `).join('');
        });
}

function getBook(){
    const id = document.getElementById("id").textContent;
    const createBookTitlePage = document.getElementById("create-book-title-page").textContent;
    const editBookTitlePage = document.getElementById("edit-book-title-page").textContent;
    if(id==0){
        document.title = `${createBookTitlePage}`;
    }else{
        const path=document.getElementById('data-rest-base-path').textContent;
        document.title = `${editBookTitlePage} '...'`;
        fetch(`${path}books/${id}`)
            .then(response => {
                let errorText;
                if (response.ok) {
                    return response.json();
                }else if (response.status === 403) {
                    errorText = document.getElementById('connection-forbidden-error-text').textContent;
                }else{
                    errorText = document.getElementById('connection-unknown-error-text').textContent;
                }
                document.getElementById('response-error-text').textContent = errorText;
            })
            .then(data => {
                const promisesAllDataBooks =
                    fetch(data._links.author.href)
                        .then(res => res.json())
                        .then(authorData =>
                            fetch(data._links.genres.href)
                                .then(res => res.json())
                                .then(genresData =>({
                                    ...data,
                                    authorHref: authorData._links.self.href,
                                    genres: genresData._embedded?.genres || []
                                }))
                        );
                promisesAllDataBooks.then(bookData => {
                    document.title = `${editBookTitlePage} '${bookData.title}'`;
                    document.getElementById("book-title").value =bookData.title;
                    document.getElementById("select-authors").value=bookData.authorHref;
                    const arrayOptions = Array.from(document.getElementById("select-genres").options);
                    bookData.genres.forEach(genre => {
                        const option=arrayOptions.find(option => option.value==genre._links.self.href);
                        if(option){
                            option.selected=true;
                        }
                    });
                });
            });
    }
}

function saveBook() {
    const id = document.getElementById("id").textContent;
    const bookTitle = document.getElementById("book-title");
    const authorsSelect = document.getElementById("select-authors");
    const genresSelect = document.getElementById("select-genres");
    const path=document.getElementById('data-rest-base-path').textContent;

    const arrayOptions = Array.from(genresSelect.options);

    const genres = arrayOptions
                        .filter(option => option.selected)
                        .map(option => option.value);

    let book = { title: bookTitle.value,
                 author: authorsSelect.value==""?null:authorsSelect.value,
                 genres: genres
    };

    let method;
    let href;
    if(id==0){
        href = '/';
    }else{
        href = `/book/${id}`;
        book = { id: `${id}`,
                 ...book
        };
    }

    fetch(`${path}books`, {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(book)})
        .then(response => {
            if (response.ok) {
                window.location.href = href;
            }else if (response.status === 403) {
                const errorText = document.getElementById('connection-forbidden-error-text').textContent;
                document.getElementById('response-error-text').textContent = errorText;
            }else{
                bookTitle.classList.add('input-error');
                authorsSelect.classList.add('input-error');
                genresSelect.classList.add('input-error');
            }

        });
}