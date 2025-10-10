window.addEventListener('load', () => {
    getBookById();
    getCommentsByBookId();
    });

function getCommentsByBookId(){
    const bookId = document.getElementById('book-id').textContent;
    const deleteTitle = document.getElementById('delete-title').textContent;

    fetch(`/api/v1/books/${bookId}/comments`)
        .then(response => {
            if (response.ok) {
              return response.json();
            }else{
                const commentsDiv = document.getElementById('comments-div');
                commentsDiv.innerHTML =``;
            }
        })
        .then(data => {
            const commentsDiv = document.getElementById('comments-div');
            commentsDiv.innerHTML = data.map(comment => `
                <div class="container-view">
                    <span>${comment.text}</span>
                    <a href="#" onclick="deleteComment(${comment.id}, '${comment.text}, ${bookId}}')">
                        <img src="/img/deleteicon16.png" alt="${deleteTitle}" />
                    </a>
                </div>
            `).join('');
        });
}

function deleteComment(id, text, bookId){
    const commentDeleteQuestion = document.getElementById('comment-delete-question').textContent + " '" + text + "'?";
    result=confirm(commentDeleteQuestion);
    if(result){
        fetch(`/api/v1/books/${bookId}/comments/${id}`, {
            method: "DELETE"
        })
        .then(response => {
            if (response.ok) {
                const bookId = document.getElementById('book-id').textContent;
                const deleteTitle = document.getElementById('delete-title').textContent;
                getCommentsByBookId(bookId, deleteTitle);
            }
        });
    }
}

function getBookById(){
    const bookId = document.getElementById('book-id').textContent;
    const bookTitlePage = document.getElementById('book-title-page').textContent;
    fetch(`/api/v1/books/${bookId}`)
        .then(response => {
            if (response.ok) {
              return response.json();
            }else{
                document.title = `${bookTitlePage}`;
                document.getElementById('book-title').innerHTML =``;
                document.getElementById('author-full-name').innerHTML =``;
                document.getElementById('genres-names').innerHTML =``;
            }
        })
        .then(data => {
            document.title = `${bookTitlePage} '${data.title}'`;
            document.getElementById('book-title').innerHTML =`${data.title}`;
            document.getElementById('author-full-name').innerHTML =`${data.author.fullName}`;
            document.getElementById('genres-names').innerHTML =`${data.genres.map(genre => genre.name).join(', ')}`;
        });
}