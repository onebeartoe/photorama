


function timeLapseChanged(state)
{
    alert('hi');
    
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)      
        {
            var s = xmlhttp.responseText + "<br/>" + document.getElementById("logs").innerHTML;
            document.getElementById("logs").innerHTML = s;
        }
    }
    var url = "time-lapse/" + state;
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("message=hi&lname=Ford");
}

function timeLapseChanged_ORIGINAL(state)
{
    alert('hi');
    
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)      
        {
            var s = xmlhttp.responseText + "<br/>" + document.getElementById("logs").innerHTML;
            document.getElementById("logs").innerHTML = s;
        }
    }
    var url = "time-lapse/" + state;
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("message=hi&lname=Ford");
}

function timeLapseFrequencyUnitChanged(unit)
{
    alert('hiOI');
    
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)      
        {
            var s = xmlhttp.responseText + "<br/>" + document.getElementById("logs").innerHTML;
            document.getElementById("logs").innerHTML = s;
        }
    }
    var url = "frequency/unit/" + unit;
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("message=hi&lname=Ford");
}