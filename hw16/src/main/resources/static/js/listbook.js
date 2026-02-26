window.addEventListener('load', () => {
    getBooks();
    getAuthors();
    getGenres();
    });

function getBooks(offset=0){
    const bookDeleteTitle = document.getElementById('book-delete-title').textContent;
    const totalPagesElement=document.getElementById('book-total-pages');
    const pageNumberElement=document.getElementById('book-page-number');
    const path=document.getElementById('data-rest-base-path').textContent;

    const totalPages=parseInt(totalPagesElement.textContent);
    const pageNumber=parseInt(pageNumberElement.textContent) - 1 + offset;

    if(pageNumber < 0 || pageNumber >= totalPages){
        return;
    }

    fetch(`${path}books?page=${pageNumber}&size=2`)
        .then(response => {
            let errorText;
            if (response.ok) {
              return response.json();
            }else if (response.status === 403) {
                errorText = document.getElementById('connection-forbidden-error-text').textContent;
            }else if (response.status === 404) {
                return [];
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
            }
            const booksTableBody = document.querySelector("#booksTable tbody");
            booksTableBody.innerHTML =`<tr>
                                           <td>${errorText}</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                       </tr>`;
        })
        .then(data => {
            const books = data._embedded?.books || [];

            const promisesAllDataBooks = books.map(book =>
                fetch(book._links.author.href)
                    .then(res => res.json())
                    .then(authorData =>
                        fetch(book._links.genres.href)
                            .then(res => res.json())
                            .then(genresData =>({
                                ...book,
                                id: book._links.self.href.split('/').pop(),
                                authorFullName: authorData.fullName,
                                genres: genresData._embedded?.genres || [],
                                selfHref: book._links.self.href
                            }))
                    )
                );

            const booksTableBody = document.querySelector("#booksTable tbody");

            Promise.all(promisesAllDataBooks).then(booksData => {
                booksTableBody.innerHTML = booksData.map(book => `
                      <tr>
                        <td><a href="/book/${book.id}">${book.title}</a></td>
                        <td>${book.authorFullName}</td>
                        <td>${book.genres.map(genre => genre.name).join(', ')}</td>
                        <td>
                            <a href="#" onclick="deleteBook('${book.selfHref}', '${book.title}')">
                                <img src="/img/deleteicon25.png" alt="${bookDeleteTitle}" />
                            </a>
                        </td>
                      </tr>
                    `).join('');
            });

            pageNumberElement.textContent = data.page.number + 1;
            totalPagesElement.textContent = data.page.totalPages;
        });
}

function deleteBook(selfHref, title){
    const bookDeleteQuestion = document.getElementById('book-delete-question').textContent + " '" + title + "'?";
    result=confirm(bookDeleteQuestion);
    if(result){
        fetch(selfHref, {
            method: 'DELETE'
        })
        .then(response => {
            let errorText;
            if (response.ok) {
                getBooks();
                return;
            }else if (response.status === 403) {
                errorText = document.getElementById('connection-forbidden-error-text').textContent;
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
            }
            alert(errorText);
        });
    }
}

function getAuthors(offset=0){
    const totalPagesElement=document.getElementById('author-total-pages');
    const pageNumberElement=document.getElementById('author-page-number');
    const path=document.getElementById('data-rest-base-path').textContent;

    const totalPages=parseInt(totalPagesElement.textContent);
    const pageNumber=parseInt(pageNumberElement.textContent) - 1 + offset;

    if(pageNumber < 0 || pageNumber >= totalPages){
        return;
    }

    fetch(`${path}authors?page=${pageNumber}&size=2`)
        .then(response => {
            let errorText;
            if (response.ok) {
              return response.json();
            }else if (response.status === 403) {
                errorText = document.getElementById('connection-forbidden-error-text').textContent;
            }else if (response.status === 404) {
                return [];
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
            }
            const authorsTableBody = document.querySelector("#authorsTable tbody");
            authorsTableBody.innerHTML =`<tr>
                                            <td>${errorText}</td>
                                         </tr>`;
        })
        .then(data => {
            const authors = data._embedded?.authors || [];
            const authorsTableBody = document.querySelector("#authorsTable tbody");
                authorsTableBody.innerHTML = authors.map(author => `
                  <tr>
                    <td>${author.fullName}</td>
                  </tr>
                `).join('');
                pageNumberElement.textContent = data.page.number + 1;
                totalPagesElement.textContent = data.page.totalPages;
        });
}

function getGenres(offset=0){
    const totalPagesElement=document.getElementById('genre-total-pages');
    const pageNumberElement=document.getElementById('genre-page-number');
    const path=document.getElementById('data-rest-base-path').textContent;

    const totalPages=parseInt(totalPagesElement.textContent);
    const pageNumber=parseInt(pageNumberElement.textContent) - 1 + offset;

    if(pageNumber < 0 || pageNumber >= totalPages){
        return;
    }

    fetch(`${path}genres?page=${pageNumber}&size=2`)
        .then(response => {
            let errorText;
            if (response.ok) {
                return response.json();
            }else if (response.status === 403) {
                errorText = document.getElementById('connection-forbidden-error-text').textContent;
            }else if (response.status === 404) {
                return [];
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
            }
            const genresTableBody = document.querySelector("#genresTable tbody");
            genresTableBody.innerHTML =`<tr>
                                           <td>${errorText}</td>
                                        </tr>`;
        })
        .then(data => {
            const genres = data._embedded?.genres || [];
            const genresTableBody = document.querySelector("#genresTable tbody");
                genresTableBody.innerHTML = genres.map(genre => `
                  <tr>
                    <td>${genre.name}</td>
                  </tr>
                `).join('');
                pageNumberElement.textContent = data.page.number + 1;
                totalPagesElement.textContent = data.page.totalPages;
        });
}