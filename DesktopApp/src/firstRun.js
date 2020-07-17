const ipcRen = require('electron').ipcRenderer
var name;
var lab_name;
function setName()
{
    name = document.getElementById('pc_name').value;
    ipcRen.send('rec-data', name);
}
