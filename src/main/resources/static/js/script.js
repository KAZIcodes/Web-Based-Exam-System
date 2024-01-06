function showNotification(message, c) {
    let notification;
    if (c === "r"){
        notification = document.getElementById('redNotification');
    }
    else {
        notification = document.getElementById('greenNotification');
    }

    notification.textContent = message;
    notification.style.display = 'block';

    // Hide the notification after a certain period (e.g., 3 seconds)
    setTimeout(() => {
        notification.style.display = 'none';
    }, 3500); // 3000 milliseconds = 3 seconds
}



function sendData(data,properties,callback){
    let payload
    let httpObject = new XMLHttpRequest()
    httpObject.timeout = 10000
    httpObject.withCredentials = true

    httpObject.open(properties.method,"http://localhost:8081"+properties.path,true)
    if(properties.headers){
        properties.headers.forEach(function(header){
            httpObject.setRequestHeader(header.key,header.value)
        })
    }

    httpObject.onreadystatechange = function(){
        let response;
        if(httpObject.readyState === 4 && (httpObject.status === 200 || httpObject.status === 400)){
            try{
                response = JSON.parse(httpObject.responseText)
            }catch(error){
                callback({status: false, msg:"Response was not JSON!!!"})
                return;
            }
            callback(response)
        }else if(httpObject.readyState === 4){
            callback(response)
            return;
        }
    }
    httpObject.onabort = function(event){
        callback({status: false, msg:"Abort happened!!!"});
    }
    httpObject.onerror = function(event){
        callback({status: false, msg:"Unexpected error happened!!!"})
    }


    if(data){
        if(properties.json){
            payload = JSON.stringify(data)
        }else{
            payload = new FormData();
            for(key in data){
                payload.append(key,data[key])
            }
        }
        httpObject.send(payload)
    }else{
        httpObject.send()
    }
}