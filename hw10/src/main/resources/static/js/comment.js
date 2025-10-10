window.addEventListener('load', () => {
    getBookById();
    });

function saveComment() {
    const bookId = document.getElementById('book-id').textContent;
    const commentText = document.getElementById("comment-text")
    const comment = { id: 0,
                    text: commentText.value};

    fetch(`/api/v1/books/${bookId}/comments`, {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(comment)})
        .then(response => {
            if (!response.ok) {
                commentText.classList.add('input-error');
                return;
            }
            window.location.href = "/book/" + bookId;
        })
}

function getBookById(){
    const bookId = document.getElementById('book-id').textContent;
    const createCommentTitlePage = document.getElementById('create-comment-title-page').textContent;
    fetch(`/api/v1/books/${bookId}`)
        .then(response => {
            if (response.ok) {
              return response.json();
            }else{
                document.title = `${createCommentTitlePage}`;
            }
        })
        .then(data => {
            document.title = `${createCommentTitlePage} '${data.title}'`;
        });
}