window.addEventListener('load', () => {
    getBookById();
    });

function saveComment() {
    const path=document.getElementById('data-rest-base-path').textContent;
    const bookId = document.getElementById('book-id').textContent;
    const commentText = document.getElementById("comment-text");
    const bookHref = document.getElementById('book-href').textContent;
    const comment = { text: commentText.value,
                      book: bookHref};

    fetch(`${path}comments`, {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(comment)})
        .then(response => {
            if (response.ok) {
                window.location.href = "/book/" + bookId;
            } else if (response.status === 403) {
                const errorText = document.getElementById('connection-forbidden-error-text').textContent;
                document.getElementById('response-error-text').textContent = errorText;
            }else {
                commentText.classList.add('input-error');
            }
        })
}

function getBookById(){
    const path=document.getElementById('data-rest-base-path').textContent;
    const bookId = document.getElementById('book-id').textContent;
    const createCommentTitlePage = document.getElementById('create-comment-title-page').textContent;
    fetch(`${path}books/${bookId}`)
        .then(response => {
            if (response.ok) {
                return response.json();
            }else{
                document.title = `${createCommentTitlePage} '...'`;
            }
        })
        .then(data => {
            document.title = `${createCommentTitlePage} '${data.title}'`;
            document.getElementById('book-href').textContent=data._links.self.href;
        });
}