const electron = require('electron')
let si = require('systeminformation');
const osu = require('os-utils');
let os = require('os');
const powerMonitor = require('electron').remote.powerMonitor;
const app = require('electron').remote.app;
const shutdown = require('electron-shutdown-command');
const screenshot = require('screenshot-desktop')

console.log(app)
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
let cpu_mnf = document.querySelector('.cpu_mnf');
let cpu_brand = document.querySelector('.cpu_brand');
let cpu_speed = document.querySelector('.cpu_speed');
let cpu_usage = document.querySelector('.cpu_usage');
let operSys = document.querySelector('.operSys');
let mnf_brand = document.querySelector('.mnf_brand');
let mnf_model = document.querySelector('.mnf_model');
let mem_total = document.querySelector('.mem_total');
let mem_used = document.querySelector('.mem_used');
let mem_ava = document.querySelector('.mem_ava');
let up_time = document.querySelector('.up_time');
let status = document.querySelector('.status');
let pcnumber = document.querySelector('.pcnumber');
let labNumber = document.querySelector('.labNumber');




const fs = require('fs')
let someString;
let labName, docName;
fs.readFile('./Output.txt', (err, data) => { 
    if (err) throw err;
    //console.log("FILE DATA : " + data.toString());
    someString = data.toString();
    index = someString.indexOf(" ");  // Gets the first index where a space occours
    docName = someString.substr(0, index); // Gets the first part
    labName  = someString.substr(index + 1);
    labName = labName.toLocaleLowerCase();
    docName = docName.toLocaleLowerCase();
    console.log("DocName : " + docName + "CollectionName : " + labName)
    console.log(labName, docName)
db.collection('LAB2').doc(labName).set({

})
db.collection('LAB').doc('LAB').collection(labName).doc(docName).set({
    shutDown : false,
    logOff : false,
    hibernate : false,
    Status : "PC is Inactive",
    mouseMovement : "not-idle",
    path : '',
    screenshot : false
})
pcnumber.innerHTML = docName;
labNumber.innerHTML = labName;
// const notification = {
//     title : 'ShutDown Alert',
//     body : 'Sysytem is About to ShutDown'
// }
// const myNoti = new window.Notification(notification.title, notification.body)
// var json = {
//     "cpu_mnf" : "1.2",
//     "cpu_speed" : "2.4"
// }
// const request = net.request({
//     method: 'POST',
//     protocol: 'http',
//     hostname: '127.0.0.1',
//     port: 8080,
//     path: '/we'
// })

db.collection('LAB').doc('LAB').collection(labName).doc(docName)
    .onSnapshot({
        includeMetadataChanges: true
    }, function(doc) {
        if(doc.data().shutDown)
        {
            console.log("System is Shutting Down");
            db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
                shutDown : false
            }).then(function () {
                setTimeout(function()
                {
                    shutdown.shutdown(); 
                }, 3000);
            })
            
        }
        if(doc.data().logOff)
        {
            console.log("System is Logginf Off");
            db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
                logOff : false
            }).then(function () {
                setTimeout(function()
                {
                    shutdown.logoff(); 
                }, 3000);
            })
            
        }
        if(doc.data().hibernate)
        {
            console.log("System is Hibernating");
            db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
                hibernate : false
            }).then(function () {
                setTimeout(function()
                {
                    shutdown.hibernate(); 
                }, 3000);
            })
            
        }
        if(doc.data().screenshot)
        {
            db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
                screenshot : false
            }).then(function () {
                screenshot().then((img) => {
                    console.log(img);
                    var metadata = {
                     contentType: 'image/jpg',
                   };
                    var storageRef = firebase.storage().ref('ScreenShots').child('shots');
                    storageRef.put(img, metadata);
                    
                    var forestRef = firebase.storage().ref('ScreenShots').child('shots');
                     // Get metadata properties
                     forestRef.getDownloadURL().then(function(url) {
                         console.log(url);
                         db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
                             path : url, 
                         })
                     })
                 });
            })
        }
});
si.cpu(function(data) {
    cpu_mnf.innerHTML = data.manufacturer;
    cpu_brand.innerHTML = data.brand;
    cpu_speed.innerHTML = data.speed + " Ghz";
    db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
        cpu_mnf : data.manufacturer,
        cpu_brand : data.brand,
        cpu_speed : data.speed
    })
});

si.osInfo(function(data) {
    let platform = data.distro;
    operSys.innerHTML = data.distro;
    db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
        platform : platform
    })
})
si.system(function(data) {
    mnf_brand.innerHTML = data.manufacturer;
    mnf_model.innerHTML = data.model;   
    db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
        mnf_brand : data.manufacturer,
        mnf_model : data.model,
    })
});    
osu.cpuUsage(function(v){
    v = parseInt(v*100);
    cpu_usage.innerHTML = v + " %";
    db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
        cpu_usage : v  
    })
});

si.mem(function(data) {
    let totalMem = parseFloat(parseFloat(data.total)/(1024*1024*1024)).toFixed(2) + " GB";
    let usedMem = parseFloat(parseFloat(data.used)/(1024*1024*1024)).toFixed(2) ;
    let avaMem = parseFloat(parseFloat(data.available)/(1024*1024*1024)).toFixed(2) + " GB";
    let mem_per = parseInt((parseFloat(data.used)/parseFloat(data.total))*100);
    console.log(mem_per)
    mem_total.innerHTML = totalMem;
    mem_used.innerHTML = usedMem;
    mem_ava.innerHTML = avaMem;
    db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
        Total_memory : totalMem,
        Used_memory : usedMem,
        Available_memory : avaMem,
        Memory_per : mem_per 
    })
});

var hours = parseFloat(parseInt(osu.sysUptime()) / 3600);
var minutes = parseInt((hours - Math.floor(hours))*60);
up_time.innerHTML =  parseInt(hours) + " Hours " + minutes + " Minutes";
var UPtime = osu.sysUptime();
var hours = parseFloat(parseInt(osu.sysUptime()) / 3600);
var minutes = parseInt((hours - Math.floor(hours))*60)
up_time.innerHTML =  parseInt(hours) + " Hours " + minutes + " Minutes";
db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
    upTime : UPtime.toString()
});


setInterval(function () {
    var UPtime;
    osu.cpuUsage(function(v){
        v = parseInt(v*100);
        cpu_usage.innerHTML = v + " %";
        db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
            cpu_usage : v  
        })
    });
    
    si.mem(function(data) {
        let totalMem = parseFloat(parseFloat(data.total)/(1024*1024*1024)).toFixed(2) + " GB";
        let usedMem = parseFloat(parseFloat(data.used)/(1024*1024*1024)).toFixed(2);
        let avaMem = parseFloat(parseFloat(data.available)/(1024*1024*1024)).toFixed(2) + " GB";
        let mem_per = parseInt((parseFloat(data.used)/parseFloat(data.total))*100);
        console.log(mem_per)
        mem_total.innerHTML = totalMem;
        mem_used.innerHTML = usedMem;
        mem_ava.innerHTML = avaMem;
        db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
            Total_memory : totalMem,
            Used_memory : usedMem,
            Available_memory : avaMem,
            Memory_per : mem_per
        })
    });

    UPtime = osu.sysUptime();
    db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
         upTime : UPtime.toString()
    })


    var today = new Date();
    var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
    db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
         upTime : UPtime.toString(),
         sysTime : time
    })

}, 5000);
function idleLogout() {
    var t;
    window.onload = resetTimer;
    window.onmousemove = resetTimer;
    window.onmousedown = resetTimer;  // catches touchscreen presses as well      
    window.ontouchstart = resetTimer; // catches touchscreen swipes as well 
    window.onclick = resetTimer;      // catches touchpad clicks as well
    window.onkeypress = resetTimer;
    window.   
    window.addEventListener('scroll', resetTimer, true); // improved; see comments

    function yourFunction() {
        // si.mem(function(data) {
        //     memory = data.used;
        //     if(memory > 50000)
        //     {

        //         console.log("Memory not Threshold")
        //         resetTimer;
        //     }
        // });
        // osu.cpuUsage(function(v){
        //     if(v > 0.03)
        //     {
        //         console.log("CPU not Threshold")
        //         resetTimer;
        //     }
        // })
        db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
            mouseMovement : "idle"
        })
    }

    function resetTimer() {
        db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
            mouseMovement : "Not-idle"
        })
        clearTimeout(t);
        t = setTimeout(yourFunction, 5000);  // time is in milliseconds
    }
}
idleLogout();
window.onload = function() {
    yourFunction();
  };
})
// setInterval(() => {
    
//     si.mem(function(data) { 
//         let mem_usage_per_sec ;        
//         mem_usage_per_sec = "MEMORY USED :" + ((parseFloat(parseFloat(data.used)/(1024*1024*1024)).toFixed(4))).toString() + "\n";
//         var xhr = new XMLHttpRequest();
//         xhr.open("POST", 'http://127.0.0.1:5000/', true);
//         xhr.send(mem_usage_per_sec);
//     });
//     // osu.cpuUsage(function (v) {
//     //     let cpu_usage_per_sec ;
//     //     v = parseFloat(v*100).toFixed(4);
//     //     v = v.toString();
//     //     cpu_usage_per_sec = "CPU USED : " + v + "\n";
          
//     // });
    
//     // var date = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
//     // var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
//     // var dateTime = date+' '+time;
//     // var pass_data =  dateTime + "\n\n";
    
//     // console.log(pass_data)
      
// }, 1000);


// console.log(si.time())
// const ioHook = require('iohook');

// let mouseX1, mouseY1, mouseX2, mouseY2;
// ioHook.on('mousemove', event => {
//     // db.collection('LAB').doc('LAB').collection(labName).doc(docName).update({
//     //     cursorX : event.x,
//     //     cursorY : event.y
//     // })
//     mouseX1 = event.x;
//     mouseY1 = event.y;
// });

// ioHook.start();
// ioHook.start(true);


// var name = " ";
// name = docName + " " + labName + " " ;
// const axios = require('axios');
// axios({
//     method: 'post',
//     url: 'http://192.168.43.192:8000',
//     data: name,
//     proxy: {
//         host: '0.0.0.0',
//         port: 5000,
//     },
// });





