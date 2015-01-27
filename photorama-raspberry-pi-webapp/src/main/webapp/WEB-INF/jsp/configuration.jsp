
<script type="text/javascript" src="${pageContext.request.contextPath}/camera.js"></script>

	<div class="copyspace">
	    <h3>onebeartoe photorama configuration</h3>
            
            <div class="featuredProject">
                <div style="margin-left: auto; margin-right: auto; width: 50%;">
                    Mode:
                    <select name="mode" onchange="modeChanged(this.value);">
                        <option value="time-lapse">Time Lapse</option>
                        <option value="photo-booth">Photo Booth</option>
                        <option value="foot-pedal">Foot Pedal</option>
                    </select>

                    <br/>
                    <br/>
                    <div id="time-lapse-controls">
                        Frequency: 
                        <select id="fequency" onchange="timeLapseFrequencyChanged(this.value);"
                            <c:forEach var="x" begin="0" end="60">
                                <option value="<c:out value="${x}"></c:out>">${x}</option>                        
                            </c:forEach>
                        </select>

                        &nbsp;

                        <select id="fequencyUnit" onchange="timeLapseFrequencyUnitChanged(this.value);">
                            <option value="seconds">Seconds</option>
                            <option value="minutes">Minutes</option>                            
                            <option value="hours">Hours</option>
                            <option value="seconds">Days</option>
                        </select>

                        <br/>
                        <br/>

                        Time Lapse:
                        <select id="timeLapse" onchange="timeLapseChanged(this.value);" >
                            <option value="off">Off</option>
                            <option value="on" >On</option>
                        </select>
                    </div>
                </div>
	    </div>
	</div>

        <div class="copyspace">
            <h3>logs</h3>
            
            <div class="featuredProject">
                <div id="logs" class="logs" style="">

                </div>
            </div>
        </div>

	<div class="copyspace">
	    <h3>onebeartoe photorama</h3>

            <div class="featuredProject">
                <center>
                    Welcome to onebeartoe Photorama.  Use the controls above to 
                    configure the camera and time lapse setting.
                </center>
	    </div>
	</div>
