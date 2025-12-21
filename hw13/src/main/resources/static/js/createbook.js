window.addEventListener('load', () => {
    getAuthors();
    getGenres();
    getBook();
    });

function getAuthors(){
    const createBookChooseAuthor = document.getElementById("create-book-choose-author").textContent;

    fetch("/api/v1/authors")
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
            const selectAuthors = document.getElementById("select-authors");
            selectAuthors.innerHTML = `
                <option value="" disabled selected hidden>${createBookChooseAuthor}</option>
                ${data.map(author => `
                      <option value="${author.id}">${author.fullName}</option>
                    `).join('')}`;
        });
}

function getGenres(){
    fetch("/api/v1/genres")
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
            const selectGenres = document.getElementById("select-genres");
                selectGenres.innerHTML = data.map(genre => `
                  <option value="${genre.id}">${genre.name}</option>
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
        document.title = `${editBookTitlePage} '...'`;
        fetch(`/api/v1/books/${id}`)
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
                document.title = `${editBookTitlePage} '${data.title}'`;
                document.getElementById("book-title").value =data.title;
                document.getElementById("select-authors").value=data.author.id;
                const arrayOptions = Array.from(document.getElementById("select-genres").options);
                data.genres.forEach(genre => {
                    const option=arrayOptions.find(option => option.value==genre.id);
                    if(option){
                        option.selected=true;
                    }
                });
            });
    }
}

function saveBook() {
    const id = document.getElementById("id").textContent;
    const bookTitle = document.getElementById("book-title");
    const authorsSelect = document.getElementById("select-authors");
    const genresSelect = document.getElementById("select-genres");

    const arrayOptions = Array.from(genresSelect.options);

    const genres = arrayOptions
                        .filter(option => option.selected)
                        .map(option => ({ id: option.value, name: "" }));

    const book = {  id: id,
                    title: bookTitle.value,
                    author: authorsSelect.value==""?null:{ id: authorsSelect.value, fullName: "" },
                    genres: genres
    };

    let method;
    let href;
    if(id==0){
        method = "POST";
        href = "/";
    }else{
        method = "PUT";
        href = "/book/" + id;
    }

    fetch(`/api/v1/books`, {
            method: method,
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