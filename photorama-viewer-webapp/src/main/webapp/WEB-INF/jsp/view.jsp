
	<div class="copyspace">
	    <h3>Go Spurs Show! - ${model.path}</h3>

            <div class="featuredProject">
                <h3>Directories:</h3>
                
                <br/>
                
                <div id="thumb_holder">
                    <c:forEach var="directory" items="${model.directories}">
                        <div class="thumb"
                             style="margin-left: auto; margin-right: auto;">
                            <a href="${pageContext.request.contextPath}/view${model.path}/${directory}">
                                <img src="${pageContext.request.contextPath}/folder.gif" width="100" height="100" alt="an image representing a directory on the filesystem" />
                            </a>
                        
                            <br />
                            <p>${directory}</p>
                        </div>
                    </c:forEach>
                    
                    <div class="spacer">&nbsp;</div>
                </div>
	    </div>
                    
            <div class="featuredProject">
                <h3>Images:</h3>
                
                <br/>
                
                <div id="thumb_holder">
                    <c:forEach var="image" items="${model.images}">
                        <div class="thumb"
                             style="margin-left: auto; margin-right: auto;">
                            <a href="${pageContext.request.contextPath}${model.path}/${image}">
                                <img src="${pageContext.request.contextPath}${model.path}/${image}" width="100" height="100" alt="an image on the filesystem" />
                            </a>
                        
                            <br />
                            <p>${image}</p>
                        </div>
                    </c:forEach>
                    
                    <div class="spacer">&nbsp;</div>
                </div>
	    </div>
	</div>
