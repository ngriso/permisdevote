var start = function() {
    p2v = {};
    p2v.model = {};
    p2v.username = "nicolas";
    p2v.urls = {};
    p2v.urls.themes = "./api/themes";
    p2v.urls.candidacies = "./api/candidacies";
    p2v.urls.voters = "./api/voters";
    p2v.urls.questions = "./api/questions";
    p2v.urls.questions_next = p2v.urls.questions + "/next?{:type}={:typeId}";
    p2v.urls.answer = p2v.urls.questions + "/{:questionId}/answer?answer={:answer}&username={:username}";

    $.when(
            $.get(p2v.urls.candidacies, function(data) {
                p2v.model.candidacies = data;
            }),
            $.get(p2v.urls.themes, function(data) {
                p2v.model.themes = data;
            })
    ).done(function() {
                var dirForCandidacies = {
                    'div' :{
                        'candidacy <- candidacies' :{
                            '.': "#{candidacy.candidate1.firstName}  #{candidacy.candidate1.lastName}",
                            '.@data-id': "candidacy.id",
                            '.@data-type': function() {
                                return "candidacyId";
                            },
                            '.@data-info': "#{candidacy.candidate1.firstName}  #{candidacy.candidate1.lastName}"
                        }
                    }
                };
                var dirForThemes = {
                    'div' :{
                        'theme <- themes' :{
                            '.': "theme.name",
                            '.@data-id': "theme.id",
                            '.@data-type': function() {
                                return "tagId";
                            },
                            '.@data-info': "theme.name"
                        }
                    }
                };

                $("#selection div.candidacies").render(p2v.model, dirForCandidacies);
                $("#selection div.themes").render(p2v.model, dirForThemes);
                $("#selection div div.selectionItem").click(clickOnCandidacyOrTheme);
                $("img", "#compte").click(function() {
                    var profile = {username: $("#username").val()};
                    $.ajax({
                        type:'POST',
                        url:p2v.urls.voters,
                        data:JSON.stringify(profile),
                        contentType:"application/json"
                    });
                });
            });

    $("#nextQuestionBtn").click(nextQuestion);
};

var clickOnCandidacyOrTheme = function (event) {
    var type = $(event.currentTarget).attr("data-type");
    var id = $(event.currentTarget).attr("data-id");
    p2v.currentInfo = $(event.currentTarget).attr("data-info");
    console.log(formatString("Selected badge: type:[{:type}], id:[{:id}]", {type:type, id:id}));
    var questionUrl = formatString(p2v.urls.questions_next, {type:type, typeId:id});
    nextQuestion(questionUrl, type);
    var regex = /(.*)[#]?/;
    var match = regex.exec(window.location.href);
    window.location.href = match[1] + "#question";
};

var nextQuestion = function(questionUrl, type) {
    $.get(questionUrl, function(data) {
        if (type === 'tagId') {
            $(".questionCandidacyText").hide();
            var dirForQuestionTheme = {
                'input@data-questionId':"question.id",
                'span[data-selector="currentInfo"]':"currentInfo",
                'span[data-selector="candidate"]':"#{question.candidacy.candidate1.firstName} #{question.candidacy.candidate1.lastName}",
                'span[data-selector="question"]':"question.text"
            };
            $(".questionThemeText").render({question:data, currentInfo: p2v.currentInfo}, dirForQuestionTheme);
            $(".questionThemeText").show();
        } else {
            $(".questionThemeText").hide();
            var dirForQuestionCandidacy = {
                'input@data-questionId':"question.id",
                'span[data-selector="currentInfo"]':"currentInfo",
                'span[data-selector="theme"]':"question.tagLevel1.name",
                'span[data-selector="question"]':"question.text"
            };
            $(".questionCandidacyText").render({question:data, currentInfo: p2v.currentInfo}, dirForQuestionCandidacy);
            $(".questionCandidacyText").show();
        }

        $(":radio").click(function(event) {
            var value = $(event.currentTarget).val();
            var questionId = $(".questionText :hidden").attr("data-questionId");
            p2v.username = $("#username").val();
            var answerURL = formatString(p2v.urls.answer, {questionId:questionId, answer:value, username:p2v.username});
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

var formatString = function(string, params) {
    for (var key in params) {
        if (params.hasOwnProperty(key)) {
            var value = params[key];
            string = string.replace("{:" + key + "}", value);
        }
    }
    return string;
};