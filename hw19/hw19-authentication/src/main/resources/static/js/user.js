window.addEventListener('load', () => {
    getRoles();
    getUser();
    });

function getRoles(){
    fetch("/api/v1/roles")
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
            const selectRoles = document.getElementById("select-roles");
            selectRoles.innerHTML =`<option value="" disabled>${errorText}</option>`;
        })
        .then(data => {
            const selectRoles = document.getElementById("select-roles");
                selectRoles.innerHTML = data.map(role => `
                  <option value="${role.id}">${role.name}</option>
                `).join('');
        });
}

function getUser(){
    const id = document.getElementById("id").textContent;
    const createUserTitlePage = document.getElementById("create-user-title-page").textContent;
    const editUserTitlePage = document.getElementById("edit-user-title-page").textContent;
    if(id==0){
        document.title = `${createUserTitlePage}`;
    }else{
        document.title = `${editUserTitlePage} '...'`;
        fetch(`/api/v1/users/${id}`)
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
                document.title = `${editUserTitlePage} '${data.login}'`;
                document.getElementById("user-login").readOnly = true;
                document.getElementById("user-login").value = data.login;
                document.getElementById("user-last-name").value = data.lastName;
                document.getElementById("user-first-name").value = data.firstName;
                document.getElementById("user-middle-name").value = data.middleName;
                document.getElementById("select-active").value = data.active ? 1 : 0;
                const arrayOptions = Array.from(document.getElementById("select-roles").options);
                data.roles.forEach(role => {
                    const option=arrayOptions.find(option => option.value==role.id);
                    if(option){
                        option.selected=true;
                    }
                });
            });
    }
}

function saveUser() {

    const id = document.getElementById("id").textContent;
    const userLogin = document.getElementById("user-login");
    const userPassword = document.getElementById("user-password");
    const userPasswordConfirmation = document.getElementById("user-password-confirmation");
    const userLastName = document.getElementById("user-last-name");
    const userFirstName = document.getElementById("user-first-name");
    const userMiddleName = document.getElementById("user-middle-name");
    const userSelectActive = document.getElementById("select-active");
    const userSelectRoles = document.getElementById("select-roles");

    const errorSpans = new Map([
        ['login', [document.getElementById("user-login-error"), userLogin]],
        ['password', [document.getElementById("user-password-error"), userPassword]],
        ['passwordConfirm', [document.getElementById("user-password-confirmation-error"), userPasswordConfirmation]],
        ['lastName', [document.getElementById("user-last-name-error"), userLastName]],
        ['firstName', [document.getElementById("user-first-name-error"), userFirstName]],
        ['middleName', [document.getElementById("user-middle-name-error"), userMiddleName]],
        ['active', [document.getElementById("select-active-error"), userSelectActive]],
        ['roles', [document.getElementById("select-roles-error"), userSelectRoles]]
    ]);

    errorSpans.forEach(([span, input], field) => {
        span.textContent = '';
        input.classList.remove('input-error');
    });

    const arrayOptions = Array.from(userSelectRoles.options);

    const roles = arrayOptions
                        .filter(option => option.selected)
                        .map(option => ({ id: option.value, name: "" }));

    const user = {  id: id,
                    login: userLogin.value,
                    password: userPassword.value===""?null:userPassword.value,
                    passwordConfirm: userPasswordConfirmation.value===""?null:userPasswordConfirmation.value,
                    lastName: userLastName.value,
                    firstName: userFirstName.value,
                    middleName: userMiddleName.value,
                    active: userSelectActive.value != 0,
                    roles: roles
    };

    let method;
    if(id==0){
        method = "POST";
    }else{
        method = "PUT";
    }

    fetch(`/api/v1/users`, {
            method: method,
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)})
        .then(response => {
            let errorText;
            if (response.ok) {
                window.location.href = "/admin";
            }else if (response.status === 403) {
                const errorText = document.getElementById('connection-forbidden-error-text').textContent;
            }else if(response.status === 400){
                return response.json();
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
            }
            document.getElementById('response-error-text').textContent = errorText;
        }).then(data => {
            Object.entries(data).forEach(([field, messages]) => {
                const errorElements = errorSpans.get(field);
                    if (errorElements) {
                        const [span, input] = errorElements;
                        span.textContent = messages.join(', ');
                        input.classList.add('input-error');
                    }
            });
        });
}