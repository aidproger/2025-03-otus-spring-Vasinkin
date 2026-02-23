window.addEventListener('load', () => {
    getBookById();
    getCommentsByBookId();
    });

function getCommentsByBookId(){
    const bookId = document.getElementById('book-id').textContent;
    const deleteTitle = document.getElementById('delete-title').textContent;
    const path=document.getElementById('data-rest-base-path').textContent;

    fetch(`${path}books/${bookId}/comments`)
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
            const commentsDiv = document.getElementById('comments-div');
            commentsDiv.innerHTML =`<div class="container-view">
                                        <span>${errorText}</span>
                                    </div>`;
        })
        .then(data => {
            const comments = data._embedded?.comments || [];
            const commentsDiv = document.getElementById('comments-div');
            commentsDiv.innerHTML = comments.map(comment => `
                <div class="container-view">
                    <span>${comment.text}</span>
                    <a href="#" onclick="deleteComment('${comment._links.self.href}', '${comment.text}', ${bookId})">
                        <img src="/img/deleteicon16.png" alt="${deleteTitle}" />
                    </a>
                </div>
            `).join('');
        });
}

function deleteComment(selfHref, text, bookId){
    const commentDeleteQuestion = document.getElementById('comment-delete-question').textContent + " '" + text + "'?";
    result=confirm(commentDeleteQuestion);
    if(result){
        fetch(selfHref, {
            method: 'DELETE'
        })
        .then(response => {
            let errorText;
            if (response.ok) {
                const bookId = document.getElementById('book-id').textContent;
                const deleteTitle = document.getElementById('delete-title').textContent;
                getCommentsByBookId(bookId, deleteTitle);
            }else if (response.status === 403) {
                errorText = document.getElementById('connection-forbidden-error-text').textContent;
                alert(errorText);
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
                alert(errorText);
            }
        });
    }
}

function getBookById(){
    const bookId = document.getElementById('book-id').textContent;
    const bookTitlePage = document.getElementById('book-title-page').textContent;
    const path=document.getElementById('data-rest-base-path').textContent;

    fetch(`${path}books/${bookId}`)
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
            document.title = `${bookTitlePage}`;
            document.getElementById('book-title').innerHTML =``;
            document.getElementById('author-full-name').innerHTML =``;
            document.getElementById('genres-names').innerHTML =``;
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
                                authorFullName: authorData.fullName,
                                genres: genresData._embedded?.genres || []
                            }))
                    );
            promisesAllDataBooks.then(bookData => {
                    document.title = `${bookTitlePage} '${bookData.title}'`;
                    document.getElementById('book-title').innerHTML = bookData.title;
                    document.getElementById('author-full-name').innerHTML = bookData.authorFullName;
                   document.getElementById('genres-names').innerHTML =bookData.genres.map(genre => genre.name).join(', ');
                });
        });
}