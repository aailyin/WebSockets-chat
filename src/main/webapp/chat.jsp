<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link type="text/css" href="resources/css/default.css" rel="stylesheet" />
    <link type="text/css" href="resources/css/smoothness/jquery-ui-1.8.22.custom.css" />
    <script type="text/javascript" src="resources/js/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="resources/js/jquery-ui-1.8.22.custom.min.js"></script>
    <script type="text/javascript" src="resources/js/wsclient.js"></script>
    <title>Conversations</title>
    <script type="text/javascript">
        $(function(){
            $('#conversations').tabs();
        });
    </script>
</head>
<body class="splash">
    <div id="wrap">

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