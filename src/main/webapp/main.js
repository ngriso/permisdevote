var nextQuestion = function() {
    console.log("Next question for: " + p2v.currentBadge);
    var questionUrl = "/api/questions?" + p2v.currentBadge;
    $.get(questionUrl, function(data) {

        if (p2v.typeInfo === 'tagId') {
            $(".questionText").html($("#questionThemeTmpl").tmpl({question:data, currentInfo: p2v.currentInfo}));
        } else {
            $(".questionText").html($("#questionTmpl").tmpl({question:data, currentInfo: p2v.currentInfo}));
        }

        $(":radio").click(function(event) {
            var value = $(event.currentTarget).val();
            var questionId = $(".questionText :hidden").attr("data-questionId");
            p2v.username = $("#username").val();
            var answerURL = "/api/questions/" + questionId + "?answer=" + value + "&username=" + p2v.username;
            console.log(answerURL);
            $.get(answerURL, function(data) {
                console.log(data);
                p2v.username = $("#username").val();

                $.get("/api/stats/" + p2v.username, function(data) {
                    $(".badges").html($("#badgesTmpl").tmpl(data));
                    nextQuestion();
                });
            });
        });
    });
};

var selectBadge = function (type, id, currentInfo) {
    p2v.typeInfo = type;
    p2v.currentBadge = type + "=" + id;
    p2v.currentInfo = currentInfo;
    console.log("Current badge: " + p2v.currentBadge);
    nextQuestion();
    var regex = /(.*)#/;
    var match = regex.exec(window.location.href);
    console.log(match[0]);
    window.location.href = match[0] + "question";
}

var scrolling = function() {
    var speed = 1000;
    jQuery('a[href^="#"]').bind('click', function() {
        var id = jQuery(this).attr('href');
        if (id == '#')
            goTo('body');
        else
            goTo(id);
        return(false);
    });
    function goTo(ancre) {
        jQuery('html,body').animate({scrollTop:jQuery(ancre).offset().top}, speed, 'swing', function() {
            if (ancre != 'body')
                window.location.hash = ancre;
            else
                window.location.hash = '#';
            jQuery(ancre).attr('tabindex', '-1');
            jQuery(ancre).focus();
            jQuery(ancre).removeAttr('tabindex');
        });
    }
};