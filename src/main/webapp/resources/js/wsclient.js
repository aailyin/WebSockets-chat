/**
 * Created with IntelliJ IDEA.
 * User: ailyin
 * Date: 28.01.13
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
var wsclient = (function(){

    var ws = null;
    var wsURI = 'ws://'+location.host+'/chat';

    function connect(userName) {

        if(!userName || userName == ''){
            return;
        }

        if('WebSocket' in window){
            ws = new WebSocket(wsURI + '?userName=' + userName);
        } else if('MozWebSocket' in window){
            ws = new MozWebSocket(wsURI + '?userName=' + userName);
        } else {
            alert("Your browser doesn't support WebSockets");
            return;
        }

        ws.onopen = function () {
            setConnected(true);
        };
        ws.onmessage = function (event) {
            var message = JSON.parse(event.data);
            processMessage(message);
        };

        ws.onclose = function () {
            setConnected(false);
            document.getElementById('userName').value = '';
            closeAllConversations();
        };

        function processMessage(message) {
            if (message.messageInfo) {
                showConversation(message.messageInfo.from);
                addMessage(message.messageInfo.from, message.messageInfo.message, cleanWhitespaces(message.messageInfo.from) + 'conversation');
            } else if (message.statusInfo) {
                if (message.statusInfo.status == 'CONNECTED') {
                    addOnlineUser(message.statusInfo.user);
                } else if (message.statusInfo.status == 'DISCONNECTED') {
                    removeOnlineUser(message.statusInfo.user);
                }
            } else if (message.connectionInfo) {
                var activeUsers = message.connectionInfo.activeUsers;
                for (var i=0; i<activeUsers.length; i++) {
                    addOnlineUser(activeUsers[i]);
                }
            }
        }
    }

    function disconnect() {
        if (ws != null) {
            ws.close();
            ws = null;
        }
        setConnected(false);
    }

    function setConnected(connected) {
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        cleanConnectedUsers();
        if (connected) {
            updateUserConnected();
        } else {
            updateUserDisconnected();
        }
    }
    function updateUserConnected() {
        var inputUsername = $('#userName');
        var onLineUserName = $('.onLineUserName');
        onLineUserName.html(inputUsername.val());
        inputUsername.css({display:'none'});
        onLineUserName.css({visibility:'visible'});
        $('.leftPanel').css({
            borderRight: '1px dashed #959595',
            paddingRight: '3em',
            visibility: 'visible'
        });
        $('#online').css({visibility: 'visible'});
        $('#splashVideo').css({visibility: 'hidden'});
        $('#beta').css({visibility: 'hidden'});
        $('#onLineUsersPanel').css({visibility:'visible'});
        $('#wrap').css({overflowY: 'auto'});
    }

    function updateUserDisconnected() {
        $('.leftPanel').css({visibility:'hidden'});
        $('.onLineUserName').css({visibility:'hidden'});
        $('#online').css({visibility: 'hidden'});
        $('#beta').css({visibility: 'visible'});
        $('#splashVideo').css({visibility: 'visible'});
        $('#userName').css({display:''});
        $('#onLineUsersPanel').css({visibility:'hidden'});
        $('#wrap').css({overflowY: 'hidden'});
        $('html, body').css({height: '100%', scrollTop: '0'});
        window.location.reload();
    }

    function cleanConnectedUsers() {
        $('#onlineUsers').html('');
    }

    function removeTab(conversationId) {
        $('#conversations').tabs('remove', conversationId);
    }

    function cleanWhitespaces(text) {
        return text.replace(/\s/g,"_");
    }

    function showConversation(from) {
        var conversations = $('#conversations');
        conversations.css({visibility:'visible'});
        var conversationId = cleanWhitespaces(from) + 'conversation';
        if(document.getElementById(conversationId) == null) {
            createConversationPanel(from);
            conversations.tabs('add', '#' + conversationId, from);
        }
        conversations.tabs('select', '#' + conversationId);
        $('#'+conversationId+'message').focus();
    }

    function createConversationPanel(name) {
        var conversationId = cleanWhitespaces(name) + 'conversation';
        var conversationPanel = $(document.createElement('div'));
        conversationPanel.attr({id : conversationId, class : 'conversation'});
        $('<p class="messages"></p><textarea id="' + conversationId + 'message"></textarea>').appendTo(conversationPanel);
        var sendButton = createSendButton(name);
        sendButton.appendTo(conversationPanel);
        var closeButton = createCloseButton(cleanWhitespaces(name));
        closeButton.appendTo(conversationPanel);
        conversationPanel.appendTo($('#conversations'));
    }

    function createSendButton(name) {
        var conversationId = cleanWhitespaces(name) + 'conversation';
        var button = $(document.createElement('button')).addClass('buttonTab');

        button.html('Send');
        button.click(function () {
            var from = document.getElementById('userName').value;
            var message = document.getElementById(conversationId+'message').value;
            toChat(from, name, message);
            addMessage(from, message, conversationId);
            document.getElementById(conversationId+'message').value = '';
        });
        return button;
    }

    function closeAllConversations() {
        for (var i = $('#conversations').tabs('length'); i >= 0; i--) {
            $('#conversations').tabs('remove', i-1);
        }
        $('#conversations').css({visibility : 'hidden'});
    }

    function createCloseButton(conversationId) {
        var button = $(document.createElement('button')).addClass('buttonTab');
        button.html('Close');
        button.click(function () {
            removeTab(conversationId);
        });
        return button;
    }

    function addMessage (from, message, conversationPanelId) {
        var messages = $('#' + conversationPanelId + ' .messages');
        var time = new Date();
        var dd = time.getDate();
        var mm = time.getMonth()+1;
        var yyyy = time.getFullYear();
        if(dd < 10){
            dd='0'+dd
        }
        if(mm < 10){
            mm = '0'+mm;
        }
        time = dd+'/'+mm+'/'+yyyy+' '+time.getHours()+':'+time.getMinutes();
        $('<div class="message"><span><b>' + from + '</b> said [' + time.valueOf() + ']:</span><p>' + $('<p/>').text(message).html() + '</p></div>').appendTo(messages);
        messages.scrollTop(messages[0].scrollHeight);
        $('#'+conversationPanelId+' textarea').focus();
    }

    function toChat(sender, receiver, message) {
        ws.send(JSON.stringify({messageInfo : {from : sender, to : receiver, message : message}}));
    }


    function addOnlineUser(userName) {
        var newOnlineUser = createOnlineUser(userName);
        newOnlineUser.appendTo($('#onlineUsers'));
    }

    function removeOnlineUser(userName) {
        $('#onlineUsers > li').each(function (index, elem) {
            if (elem.id == userName + 'onlineuser') {
                $(elem).remove();
            }
        });
    }

    function createOnlineUser(userName) {
        var link = $(document.createElement('a'));
        link.html(userName);
        link.click(function(){
            showConversation(userName);
        });
        var li = $(document.createElement('li'));
        li.attr({id : (userName + 'onlineuser')});
        link.appendTo(li);
        return li;
    }

    return {
        connect : connect,
        disconnect : disconnect
    };
})();

/*Function for animation of input*/
var inputSibling = (function (){
    $('.username-label-sliding').animate({ opacity: "0.4" })
        .click(function() {
            var thisFor	= $(this).attr('for');
            $('.'+thisFor).focus();
        });

    $('.username-sliding').focus(function() {

        $('.username-label-sliding').animate({ marginLeft: "4.4em" }, "fast");

        if($(this).val() == "username")
            $(this).val("");

    }).blur(function() {

            if($(this).val() == "") {
                $('.username-label-sliding').animate({ marginLeft: "12px" }, "fast");
            }
        });
});