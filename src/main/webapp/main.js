var application = {
    initialize:function() {
        application.model = {};
        application.data = {
            userID : "",
            currentType : "",
            currentSelectedTypeId:""
        };
        application.urls = {
            themes : "./api/themes",
            candidacies : "./api/candidacies",
            voters : "./api/voters",
            voter_stats : "./api/myStats",
            next_question: function() {
                return "./api/questions/next?" + (application.data.currentType === 'tagId' ? "tagId=" : "candidacyId=") + application.data.currentSelectedTypeId
            },
            answer : function(questionID, answer) {
                return "./api/questions/" + questionID + "/answer?answer=" + answer
            }
        };
        var dirForBadgesCandidacies = {
            'div' : {
                'statCandiday <- statsCandidacy':{
                    '.': "#{statCandiday.candidacy.candidate1.firstName} #{statCandiday.candidacy.candidate1.lastName} #{statCandiday.rights} / #{statCandiday.answered}"
                }
            }
        };
        application.cachedTemplateForBadgesCandidacies = $("div.badges h2.badgesCandidacies").compile(dirForBadgesCandidacies);
        var dirForBadgesThemes = {
            'div' : {
                'statTheme <- statsTheme':{
                    '.': "#{statTheme.tag.name} #{statTheme.rights} / #{statTheme.answered}"
                }
            }
        };
        application.cachedTemplateForBadgesThemes = $("div.badges h2.badgesThemes").compile(dirForBadgesThemes);
    },
    start : function() {
        application.initialize();
        $.when(
                $.get(application.urls.candidacies, function(data) {
                    application.model.candidacies = data;
                }),
                $.get(application.urls.themes, function(data) {
                    application.model.themes = data;
                })
        ).done(function() {
                    $("#compte img").click(application.clickOnStartPermis);
                    $(":radio").click(application.clickOnResponse);
                });
    },
    clickOnStartPermis : function() {
        var profile = {username: $("#username").val()};
        $.ajax({
            type:'POST',
            url:application.urls.voters,
            data:JSON.stringify(profile),
            contentType:"application/json",
            success:function(data) {
                application.data.userID = data.id;
            }
        });

        $("#selection").show();
        application.renderCandidaciesList();
        application.renderThemeList();
    },
    renderCandidaciesList:function() {
        var dirForCandidacies = {
            'div' :{
                'candidacy <- candidacies' :{
                    '.': "#{candidacy.candidate1.firstName}  #{candidacy.candidate1.lastName}",
                    '.@data-id': "candidacy.id",
                    '.@data-info': "#{candidacy.candidate1.firstName}  #{candidacy.candidate1.lastName}"
                }
            }
        };
        $("#selection div.candidacies").render(application.model, dirForCandidacies);
        $("div.selectionItem.candidacy").click(function(event) {
            application.data.currentType = "candidacyId";
            application.data.currentSelectedTypeId = $(event.currentTarget).attr("data-id");
            application.currentInfo = $(event.currentTarget).attr("data-info");
            application.nextQuestion();
        });
    },
    renderThemeList:function() {
        var dirForThemes = {
            'div' :{
                'theme <- themes' :{
                    '.': "theme.name",
                    '.@data-id': "theme.id",
                    '.@data-info': "theme.name"
                }
            }
        };
        $("#selection div.themes").render(application.model, dirForThemes);
        $(".selectionItem.theme").click(function(event) {
            application.data.currentType = "tagId";
            application.data.currentSelectedTypeId = $(event.currentTarget).attr("data-id");
            application.currentInfo = $(event.currentTarget).attr("data-info");
            application.nextQuestion();
        });
    },
    nextQuestion : function() {
        $.get(application.urls.next_question(), function(data) {
            $("div.question").attr("data-questionId", data.id);
            $(".questionThemeText").hide();
            $(".questionCandidacyText").hide();
            if (application.data.currentType === 'tagId') {
                application.renderQuestionTheme(data);
            } else {
                application.renderQuestionCandidacy(data);
            }
            $(".answers").show();
            $(".gotoNextQuestion").hide();
            $(".answers :radio").each(function() {
                $(this).prop('checked', false)
            });
        });
    },
    renderQuestionTheme:function(data) {
        var dirForQuestionTheme = {
            'span[data-selector="currentInfo"]':"currentInfo",
            'span[data-selector="candidate"]':"#{question.candidacy.candidate1.firstName} #{question.candidacy.candidate1.lastName}",
            'span[data-selector="question"]':"question.text"
        };
        $(".questionThemeText").render({question:data, currentInfo: application.currentInfo}, dirForQuestionTheme);
        $(".questionThemeText").show();
    },
    renderQuestionCandidacy:function(data) {
        var dirForQuestionCandidacy = {
            'span[data-selector="currentInfo"]':"currentInfo",
            'span[data-selector="theme"]':"question.tagLevel1.name",
            'span[data-selector="question"]':"question.text"
        };
        $(".questionCandidacyText").render({question:data, currentInfo: application.currentInfo}, dirForQuestionCandidacy);
        $(".questionCandidacyText").show();
    },
    clickOnResponse : function(event) {
        $(".answers").hide();
        $(".gotoNextQuestion").show();
        $("div.question :button").click(application.nextQuestion);
        var value = $(event.currentTarget).val();
        var questionId = $("div.question").attr("data-questionId");
        var answerURL = application.urls.answer(questionId, value);
        $.get(answerURL, function(data) {
            application.renderBadges();
            if (data) {
                $("h2.responseTextIncorrect").hide();
                $("h2.responseTextCorrect").show();
            } else {
                $("h2.responseTextCorrect").hide();
                $("h2.responseTextIncorrect").show();
            }
        });
    },
    renderBadges:function() {
        var voterStatsURL = application.urls.voter_stats;
        $.get(voterStatsURL, function(data) {
            $("div.badges h2.badgesCandidacies").render(data, application.cachedTemplateForBadgesCandidacies);
            $("div.badges h2.badgesThemes").render(data, application.cachedTemplateForBadgesThemes);
            $(".badges").show();
        });
    }
};

