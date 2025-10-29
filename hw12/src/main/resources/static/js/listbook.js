window.addEventListener('load', () => {
    getBooks();
    getAuthors();
    getGenres();
    });

function getBooks(){
    const bookDeleteTitle = document.getElementById('book-delete-title').textContent;
    fetch("/api/v1/books")
        .then(response => {
            if (response.ok) {
              return response.json();
            }else{
                const booksTableBody = document.querySelector("#booksTable tbody");
                booksTableBody.innerHTML =``;
            }
        })
        .then(data => {
            const booksTableBody = document.querySelector("#booksTable tbody");
                booksTableBody.innerHTML = data.map(book => `
                  <tr>
                    <td><a href="/book/${book.id}">${book.title}</a></td>
                    <td>${book.author.fullName}</td>
                    <td>${book.genres.map(genre => genre.name).join(', ')}</td>
                    <td>
                        <a href="#" onclick="deleteBook(${book.id}, '${book.title}')">
                            <img src="/img/deleteicon25.png" alt="${bookDeleteTitle}" />
                        </a>
                    </td>
                  </tr>
                `).join('');
        });
}

function deleteBook(id, title){
    const bookDeleteQuestion = document.getElementById('book-delete-question').textContent + " '" + title + "'?";
    result=confirm(bookDeleteQuestion);
    if(result){
        fetch(`/api/v1/books/${id}`, {
            method: "DELETE"
        })
        .then(response => {
            if (response.ok) {
                getBooks();
            }
        });
    }
}

function getAuthors(){
    fetch("/api/v1/authors")
        .then(response => {
            if (response.ok) {
              return response.json();
            }else{
                const authorsTableBody = document.querySelector("#authorsTable tbody");
                authorsTableBody.innerHTML =``;
            }
        })
        .then(data => {
            const authorsTableBody = document.querySelector("#authorsTable tbody");
                authorsTableBody.innerHTML = data.map(author => `
                  <tr>
                    <td>${author.fullName}</td>
                  </tr>
                `).join('');
        });
}

function getGenres(){
    fetch("/api/v1/genres")
        .then(response => {
            if (response.ok) {
              return response.json();
            }else{
                const genresTableBody = document.querySelector("#genresTable tbody");
                genresTableBody.innerHTML =``;
            }
        })
        .then(data => {
            const genresTableBody = document.querySelector("#genresTable tbody");
                genresTableBody.innerHTML = data.map(genre => `
                  <tr>
                    <td>${genre.name}</td>
                  </tr>
                `).join('');
        });
}