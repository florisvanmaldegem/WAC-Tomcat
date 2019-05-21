const wmKey = "68d3df80956e68999b9b1055baa5ba98";

window.addEventListener('load', init);

function init(e){
    getIpInfo();
    
}

function getIpInfo(){
    requestIpApi();
}

function requestIpApi(){
    fetch("https://ipapi.co/json")
    .then(response => response.json())
    .then(responseIpApi)
    .catch(function(error){
        console.log(error);
    });
}

function responseIpApi(data){
    AddKeyValue("#locationInfo","Landcode", data.country);
    AddKeyValue("#locationInfo","Land", data.country_name);
    AddKeyValue("#locationInfo","Regio", data.region);
    AddKeyValue("#locationInfo","Stad", data.city);
    AddKeyValue("#locationInfo","Postcode", data.postal);
    AddKeyValue("#locationInfo","Latitude", data.latitude);
    AddKeyValue("#locationInfo","Longitude", data.longitude);
    AddKeyValue("#locationInfo","IP", data.ip)
    requestWeather(data.latitude, data.longitude, data.city);
}

function AddKeyValue(addTo, key, value){
    let pairElement = document.querySelector(addTo);
    console.log(pairElement);

    let kTextNode = document.createTextNode(key + ":");
    let vTextNode = document.createTextNode(value);

    let kElement = document.createElement("p");
    let vElement = document.createElement("p");

    kElement.classList.add("key");
    vElement.classList.add("value");

    kElement.appendChild(kTextNode);
    vElement.appendChild(vTextNode);
    
    pairElement.appendChild(kElement);
    pairElement.appendChild(vElement);
}

function requestWeather(lat, lon, city){
    const url = "https://api.openweathermap.org/data/2.5/weather?"+
    "lat="+lat+"&"+
    "lon="+lon+"&"+
    "APPID="+wmKey+"&"+
    "units=metric&"+
    "lang=nl";


    fetch(url)
    .then(response => response.json())
    .then(responseWeather)
    .catch(function(error){
        console.log(error);
    });
}

function responseWeather(data){
    AddKeyValue("#weatherInfo", "Temperatuur", data.main.temp);
    AddKeyValue("#weatherInfo", "Luchtvochtigheid", data.main.humidity);
    AddKeyValue("#weatherInfo", "Windsnelheid", data.wind.speed);
    AddKeyValue("#weatherInfo", "Windrichting", data.wind.deg);
}