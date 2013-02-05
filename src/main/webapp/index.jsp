<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link type="text/css" href="resources/css/default.css" rel="stylesheet" />
    <link type="text/css" href="resources/css/smoothness/jquery-ui-1.8.22.custom.css" rel="stylesheet" />
    <script type="text/javascript" src="resources/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="resources/js/jquery-ui-1.8.22.custom.min.js"></script>
    <script type="text/javascript" src="resources/js/wsclient.js"></script>
    <title>WebSockets chat</title>
      <script type="text/javascript">
          $(function(){
              $('#conversations').tabs();
          });
      </script>
  </head>
  <body class="splash">
    <div id="wrap">
        <video id="splashVideo" class="spin" loop="loop" autoplay="autoplay" tabindex="0">
            <source type="video/mp4" src='resources/img/video1.mp4.mp4; codecs="avc1.42E01E, mp4a.40.2"'>
            </source>
            <source type="video/webm" src='resources/img/video1.webm; codecs="vp8, vorbis"'>
            </source>
            <source type="video/ogg" src='resources/img/video1.ogv; codecs="theora, vorbis"'>
            </source>
        </video>
         <div id="online">
            Username: <span class="onLineUserName"></span>
            <button id="disconnect" class="secondary" disabled="disabled" onclick="wsclient.disconnect();">Disconnect</button>
        </div>
        <div id="beta">
            <h1>
                Welcome to the
                <br>
                Exadel training chat
            </h1>
            <h3>Bulid on WebSockets Tomcat 7</h3>
            <p>Chat was built with style MySpace.com</p>
            Name: <input type="text" id="userName"/>
            <p class="actionContainer">
                <button id="connect" onclick="wsclient.connect(document.getElementById('userName').value);">Connect</button>

            </p>
        </div>
        <div id="container">
            <div class="leftPanel">
                <div id="onLineUsersPanel">
                    <h3>Connected users:</h3>
                    <ul id="onlineUsers">

                    </ul>
                </div>
            </div>

            <div id="conversations">
                <ul>
                </ul>
            </div>
        </div>
    </div>
    <footer>
        <div id="footer">
            <a href="http://www.exadel.com">
                <i class="exadel-logo"></i>
            </a>
        </div>
        <nav id="links">
            <a href="/">About</a>
            .©2013 Exadel Inc. All rights reserved.
        </nav>
    </footer>
  </body>
</html>