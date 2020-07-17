
const { app, BrowserWindow } = require('electron')
const ipcMain = require('electron').ipcMain
const firstRun = require('electron-first-run')
const { net } = require('electron')
const AutoLaunch = require('auto-launch');

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let win
const isFirstRun = firstRun()
var lab_name;

function createWindow () {
  // Create the browser window.
  win = new BrowserWindow({
    width: 1600,
    height: 800,
    webPreferences: {
      nodeIntegration: true
    }
  })
  // var json = {
  //   "cpu_mnf" : "1.2",
  //   "cpu_speed" : "2.4"
  // }
  // const request = net.request({
  //   method: 'POST',
  //   protocol: 'http',
  //   hostname: '127.0.0.1',
  //   port: 8080,
  //   path: '/we',
  //   body : json
  // })
  // and load the index.html of the app.
  if(1)
  {
    ipcMain.on('rec-data', function(event, arg) {
        const fs = require('fs')  
        let data = arg;
        fs.writeFile('Output.txt', data, (err) => {  
        if (err) throw err; 
      })
    })
     
    win.loadFile('src/firstRun.html');
  }
  else
  {
    win.loadFile('src/index.html');
  }
  // Open the DevTools.
  //win.webContents.openDevTools()

  // Emitted when the window is closed.
  win.on('closed', () => {
    // Dereference the window object, usually you would store windows
    // in an array if your app supports multi windows, this is the time
    // when you should delete the corresponding element.
    win = null
  })
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', createWindow)
// app.on('ready', function() {
//   let autoLaunch = new AutoLaunch({
//     name: 'index',
//     path: app.getPath(''),
//   });
//   autoLaunch.isEnabled().then((isEnabled) => {
//     if (!isEnabled) autoLaunch.enable();
//   });
// })
// Quit when all windows are closed.
app.on('window-all-closed', () => {
  // On macOS it is common for applications and their menu bar
  // to stay active until the user quits explicitly with Cmd + Q
  if (process.platform !== 'darwin') {
    app.quit()
  }
})

app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (win === null) {
    createWindow()
  }
})

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.

