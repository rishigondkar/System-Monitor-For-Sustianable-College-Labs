var list = document.querySelector('.list');
const shutdown = require('electron-shutdown-command');


function renderPc(doc)
{
    let li = document.createElement('li');
    //let id = document.createElement('span');
    let id = document.createElement('a');
    let br = document.createElement('br');
    let memory = document.createElement('span');
    li.setAttribute('data-id', doc.id);
    id.setAttribute('href', "www.google.com");
    id.innerText = doc.id;
    memory.textContent = doc.data().Used_memory;
    
    li.appendChild(id);
    li.appendChild(br);
    li.appendChild(memory);
    list.appendChild(li);
}
function shutdownfunction()
{
    shutdown.shutdown();
}



// AUTHENTICATION //

document.getElementById("subBtn").addEventListener("click", function(event){
    event.preventDefault()
});
var login = document.querySelector('.login');
var content = document.querySelector('.content');
var user_name = document.querySelector('.user_name');
content.setAttribute('style', 'display : none');
firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
      // User is signed in.
      var displayName = console.log(user.displayName);
      var email = console.log(user.email);
      // ...
      user_name.innerHTML = user.email;
      login.setAttribute('style', 'display : none');
      content.setAttribute('style', 'display : block');
      db.collection("Lab").get().then(function(querySnapshot) {
        querySnapshot.forEach(function(doc) {
            console.log(doc.id, " => ", doc.data());
            renderPc(doc);
        });
    });
    
    } else {
      // User is signed out.
      // ...
    }
});
function verify()
{
    var email = document.getElementById('userId').value;
    var password = document.getElementById('userPass').value;
    console.log(email);
    console.log(password);
    firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
        var errorCode = error.code;
        var errorMessage = error.message;
        alert("InValid Credetials");
        location.reload();
      });
}
function logout()
{
    firebase.auth().signOut().then(function() {

    }).catch(function(error) {
        console.log(error.message);
    })
    window.location = 'index.html';
}
