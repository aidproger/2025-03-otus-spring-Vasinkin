window.addEventListener('load', () => {
    getUsers();
    getRoles();
    getWells();
    });

function getUsers(){
    const userDeleteTitle = document.getElementById('user-delete-title').textContent;
    const usersTableActiveYes = document.getElementById('users-table-active-yes').textContent;
    const usersTableActiveNo = document.getElementById('users-table-active-no').textContent;
    const userEditTitle = document.getElementById("user-edit-title").textContent;
    fetch("/api/v1/users")
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
            const usersTableBody = document.querySelector("#usersTable tbody");
            usersTableBody.innerHTML =`<tr>
                                           <td>${errorText}</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                       </tr>`;
        })
        .then(data => {
            const usersTableBody = document.querySelector("#usersTable tbody");
                usersTableBody.innerHTML = data.map(user => `
                  <tr>
                    <td>${user.login}</td>
                    <td>${user.lastName}</td>
                    <td>${user.firstName}</td>
                    <td>${user.middleName}</td>
                    <td>${user.registrationDate}</td>
                    <td>${user.lastAccessedAt ? user.lastAccessedAt : ""}</td>
                    <td>${user.active ? usersTableActiveYes : usersTableActiveNo}</td>
                    <td>${user.roles.map(role => role.name).join(', ')}</td>
                    <td><a href="/admin/user/${user.id}">
                        <img src="/img/editicon25.png" alt="${userEditTitle}" /></a>
                    </td>
                    <td>
                        <a href="#" onclick="deleteUser(${user.id}, '${user.login}')">
                            <img src="/img/deleteicon25.png" alt="${userDeleteTitle}" />
                        </a>
                    </td>
                  </tr>
                `).join('');
        });
}

function deleteUser(id, title){
    const userDeleteQuestion = document.getElementById('user-delete-question').textContent + " '" + title + "'?";
    result=confirm(userDeleteQuestion);
    if(result){
        fetch(`/api/v1/users/${id}`, {
            method: "DELETE"
        })
        .then(response => {
            let errorText;
            if (response.ok) {
                getUsers();
                return;
            }else if (response.status === 403) {
                response.json().then(errorData => {
                        alert(errorData.message);
                    });
            }else{
                errorText = document.getElementById('connection-unknown-error-text').textContent;
                alert(errorText);
            }
        });
    }
}

function getRoles(){
    fetch("/api/v1/roles")
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
            const rolesTableBody = document.querySelector("#rolesTable tbody");
            rolesTableBody.innerHTML =`<tr>
                                           <td>${errorText}</td>
                                        </tr>`;
        })
        .then(data => {
            const rolesTableBody = document.querySelector("#rolesTable tbody");
                rolesTableBody.innerHTML = data.map(role => `
                  <tr>
                    <td>${role.name}</td>
                  </tr>
                `).join('');
        });
}

function getWells(){
    const usersTableActiveYes = document.getElementById('users-table-active-yes').textContent;
    const usersTableActiveNo = document.getElementById('users-table-active-no').textContent;
    const hostname = window.location.hostname;
    const port = 8070;
    fetch(`http://${hostname}:${port}/api/v1/wells`)
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
            const usersTableBody = document.querySelector("#wellsTable tbody");
            usersTableBody.innerHTML =`<tr>
                                           <td>${errorText}</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                           <td>-</td>
                                       </tr>`;
        })
        .then(data => {
            const usersTableBody = document.querySelector("#wellsTable tbody");
                usersTableBody.innerHTML = data.map(well => `
                  <tr>
                    <td>${well.sgtiCode}</td>
                    <td>${well.codeName}</td>
                    <td>${well.fullName}</td>
                    <td>${well.creationDate}</td>
                    <td>${well.completionDate ? well.completionDate : ""}</td>
                    <td>${well.beginAbsoluteMeter ? well.beginAbsoluteMeter : ""}</td>
                    <td>${well.endAbsoluteMeter ? well.endAbsoluteMeter : ""}</td>
                    <td>${well.archive ? usersTableActiveYes : usersTableActiveNo}</td>
                  </tr>
                `).join('');
        });
}