window.addEventListener("load", init);

let screen;

function init(e){
    screen = document.querySelector("#screen");

    let buttons = document.querySelectorAll("button");
    for(let button of buttons){
        button.addEventListener('click', function(){
            handleButton(button.innerText);
        });
    }
}

function handleButton(button){
    let numberArray = new Array("0","1","2","3","4","5","6","7","8","9");
    let signArray = new Array("+","-", "*", "/");

    if(numberArray.indexOf(button) != -1){
        addNumber(button);
    }else if(signArray.indexOf(button) != -1){
        addSign(button);
    }else if(button === "="){
        evaluate();
    }else if(button === "C"){
        clear();
    }
}


function addNumber(number){
    let currentValue = screen.value;
    currentValue = currentValue + number;
    screen.value = currentValue;
}

function addSign(sign){
    if(screen.value != ""){
        screen.value = screen.value + sign;
    }
}

function evaluate(){
    let answer = eval(screen.value);
    screen.value = answer;
}

function clear(){
    screen.value = "";
}