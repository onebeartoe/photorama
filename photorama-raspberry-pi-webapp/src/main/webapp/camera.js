
function changeControls(mode)
{
    switch(mode)
    {
        case "photo-booth":
        {
            // purposely fall into the foot pedal case            
        }
        case "foot-pedal":
        {
            hideElement("time-lapse-controls");
            
            break;
        }
        default:
        {
            // "time-lapse"
            showElement("time-lapse-controls");
        }
    }
}

function hideElement(id)
{
    var element = document.getElementById(id);
    element.style.display = 'none';
}

function httpCall()
{
    
}

function httpPost()
{
    
}

function modeChanged(mode)
{
    changeControls(mode);
    
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function()
    {
        updateLogs(xmlhttp);
    }
    var url = "mode/" + mode;
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("p1=a&p2=b");
}

function showElement(id)
{
    var element = document.getElementById(id);
    element.style.display = 'block';
}
    
function stopCamera()
{
    timeLapseChanged("off");
}

function timeLapseChanged(state)
{
//    alert('hi from time lapse state change');
    
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
    xmlhttp.send("p1=a&p2=b");
}

function timeLapseFrequencyChanged(frequency)
{
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4 && xmlhttp.status==200)      
        {
            var s = xmlhttp.responseText + "<br/>" + document.getElementById("logs").innerHTML;
            document.getElementById("logs").innerHTML = s;
        }
    }
    var url = "frequency/" + frequency;
    xmlhttp.open("POST", url, true);
    xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
    xmlhttp.send("p1=a&p2=b");
}

function timeLapseFrequencyUnitChanged(unit)
{
//    alert('hiOI');
    
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
    xmlhttp.send("p1=a&p2=b");
}

// maybe this could be renamed to logServerResponse()
function updateLogs(xmlhttp)
{
    if (xmlhttp.readyState==4 && xmlhttp.status==200)      
    {
        var s = xmlhttp.responseText + "<br/>" + document.getElementById("logs").innerHTML;
        document.getElementById("logs").innerHTML = s;
    }    
}