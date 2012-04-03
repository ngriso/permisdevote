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
                return "./api/questions/" + questionID + "/answer?answer=" + answer + "&type=" + application.data.currentType;
            }
        };
        application.templates = {};
        var dirForThemes = {
            'li' :{
                'theme <- themes' :{
                    'img@src': "img/themes/#{theme.namespace}1.png",
                    'img@alt': "theme.name",
                    '.@data-id': "theme.id",
                    '.@data-info': "theme.name"
                }
            }
        };
        application.templates.listThemes = $("div.themes").compile(dirForThemes);

        var dirForCandidacies = {
            'li' :{
                'candidacy <- candidacies' :{
                    'img@src': "img/candidates/#{candidacy.candidate1.namespace}1.png",
                    'img@alt': "#{candidacy.candidate1.firstName}  #{candidacy.candidate1.lastName}",
                    '.@data-id': "candidacy.id",
                    '.@data-info': "#{candidacy.candidate1.firstName}  #{candidacy.candidate1.lastName}"
                }
            }
        };
        application.templates.listCandidacies = $("div.candi").compile(dirForCandidacies);

        var dirForBadgesCandidacies = {
            'div' : {
                'statCandiday <- statsCandidacy':{
                    'li.resultsthemes img@src': "img/candidates/#{statCandiday.candidacy.candidate1.namespace}1.png",
                    'li.prctthemes' : function(context) {
                        if (context.item.answered === 0) {
                            return "0 %";
                        }
                        var pourcentage = context.item.rights * 100 / context.item.answered;
                        return Math.round(pourcentage) + " %";
                    },
                    'li.resultsthemes img@style': function(context) {
                        if (context.item.answered === 0) {
                            return "margin-top: 265px;"
                        }
                        var marginTop = (1 - context.item.rights / context.item.answered) * 265;
                        return "margin-top: " + marginTop +"px;"
                    }
                }
            }
        };
        application.templates.resultsCandidacies = $("div.resultsCandidacies").compile(dirForBadgesCandidacies);
        var dirForBadgesThemes = {
            'div' : {
                'statTheme <- statsTheme':{
                    'li.resultsthemes img@src': "img/themes/#{statTheme.tag.namespace}1.png",
                    'li.prctthemes' : function(context) {
                        if (context.item.answered === 0) {
                            return "0 %";
                        }
                        var pourcentage = context.item.rights * 100 / context.item.answered;
                        return Math.round(pourcentage) + " %";
                    },
                    'li.resultsthemes img@style': function(context) {
                        if (context.item.answered === 0) {
                            return "margin-top: 290px;"
                        }
                        var marginTop = (1 - context.item.rights / context.item.answered) * 290;
                        return "margin-top: " + marginTop +"px;"
                    }
                }
            }
        };
        application.cachedTemplateForBadgesThemes = $("div.resultsThemes").compile(dirForBadgesThemes);
    },
    start : function() {
        $.smAnchor(null);
        application.initialize();
        $.when(
                $.get(application.urls.candidacies, function(data) {
                    application.model.candidacies = data;
                }),
                $.get(application.urls.themes, function(data) {
                    application.model.themes = data;
                })
        ).done(function() {
                    $("#compt img").click(application.clickOnStartPermis);
                    $("div.gotoNextQuestion :button").click(application.nextQuestion);
                    $(".printMesPermisThemes").click(application.printResultsThemes);
                    $(".printMesPermisCandidacies").click(application.printResultsCandidacies);
                });
    },
    clickOnStartPermis : function() {
        var profile = {
            username: $("#username").val(),
            activity:$("#activity").val(),
            age:$("#age").val()
        };
        $.ajax({
            type:'POST',
            url:application.urls.voters,
            data:JSON.stringify(profile),
            contentType:"application/json",
            success:function(data) {
                application.data.userID = data.id;
            }
        });
        $.when(
                application.renderCandidaciesList(),
                application.renderThemeList()
        ).done(function() {
                    $("#selection").show();
                });
    },
    renderCandidaciesList:function() {
        $("div.candi").render(application.model, application.templates.listCandidacies);
        $("li.candi2").hover(function(event) {
            $("h1.permis_choisi").html($(event.currentTarget).attr("data-info"));
        });
        $("li.candi2").click(function(event) {
            application.data.currentType = "candidacyId";
            application.data.currentSelectedTypeId = $(event.currentTarget).attr("data-id");
            application.currentInfo = $(event.currentTarget).attr("data-info");
            var srcImgPermis1 = $(event.currentTarget).find("img").attr("src");
            var srcImgPermis = srcImgPermis1.replace("1", "");
            $("img.current_permis").attr("src", srcImgPermis);
            $("span.current_permis").html(application.currentInfo);
            $("div.current_permis").show();
            application.nextQuestion();
        });
    },
    renderThemeList:function() {
        $("div.themes").render(application.model, application.templates.listThemes);
        $("li.themes2").hover(function(event) {
            $("h1.permis_choisi").html($(event.currentTarget).attr("data-info"));
        });
        $("li.themes2").click(function(event) {
            application.data.currentType = "tagId";
            application.data.currentSelectedTypeId = $(event.currentTarget).attr("data-id");
            application.currentInfo = $(event.currentTarget).attr("data-info");
            var srcImgPermis1 = $(event.currentTarget).find("img").attr("src");
            var srcImgPermis = srcImgPermis1.replace("1", "");
            $("img.current_permis").attr("src", srcImgPermis);
            $("span.current_permis").html(application.currentInfo);
            $("div.current_permis").show();
            application.nextQuestion();
        });
    },
    nextQuestion : function() {
        $.get(application.urls.next_question(), function(data) {
            $("div.reponse").attr("data-questionId", data.id);
            $(".questionThemeText").hide();
            $(".questionCandidacyText").hide();
            if (application.data.currentType === 'tagId') {
                application.renderQuestionTheme(data);
            } else {
                application.renderQuestionCandidacy(data);
            }
            $(".answers").show();
            $(".gotoNextQuestion").hide();
            $("#questions").show();
            $('a[href="#questions"]').click();
            $("div.reponse li").click(application.clickOnResponse);
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
        $("div.reponse li").off('click');
        $(".answers").hide();
        $(".gotoNextQuestion").show();
        var value = $(event.currentTarget).attr("data-value");
        var questionId = $("div.reponse").attr("data-questionId");
        var answerURL = application.urls.answer(questionId, value);
        $.get(answerURL, function(data) {
            application.renderBadges();
            if (data.correct) {
                $("h2.responseTextIncorrect").hide();
                $("h2.responseTextCorrect").show();
            } else {
                $("h2.responseTextCorrect").hide();
                $("h2.responseTextIncorrect").show();
            }
            if (data.response === false) {
                $("span.responseCandidacy").html(data.candidacyJPA.candidate1.firstName + " " + data.candidacyJPA.candidate1.lastName);
                $("h2.responseTextComplet").show();
            } else {
                $("h2.responseTextComplet").hide();
            }
        });
    },
    renderBadges:function() {
        var voterStatsURL = application.urls.voter_stats;
        $.get(voterStatsURL, function(data) {
            $("div.resultsThemes").render(data, application.cachedTemplateForBadgesThemes);
            $("div.resultsCandidacies").render(data, application.templates.resultsCandidacies);
            if (application.data.currentType === 'tagId') {
                application.printResultsThemes();
            } else {
                application.printResultsCandidacies();
            }
            $("#results").show();
        });
    },
    printResultsThemes:function() {
        $("div.resultsThemes").show();
        $("div.resultsCandidacies").hide();
    },
    printResultsCandidacies:function() {
        $("div.resultsThemes").hide();
        $("div.resultsCandidacies").show();
    }
};

