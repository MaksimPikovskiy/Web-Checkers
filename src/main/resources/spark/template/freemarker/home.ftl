<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <meta http-equiv="refresh" content="10">
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
  <style>
        .playerListings {
            margin: 0px;
            padding: 6px;
            border: 1px solid black;
        }

        input[type="submit"].spectateBtn {
            background: #906ff8;
            border: 1px solid #7a55f7;
            padding-left: 5px;
            padding-right: 5px;
        }

        input[type="submit"]:disabled {
            background: #8EA0CB;
        }
  </style>
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <!-- Provide a message to the user, if supplied. -->
    <#include "message.ftl" />

    <strong>Players Online</strong><br/>

    <#if playerSignIn>
        <#if numberPlayersOnline <= 1>
            <ol>
                There are no other players available to play at this time.
            </ol>
            <div class="playerListings">
                Challenge AI Player
                <form action="/" method="POST" style="display: inline;float: right;">
                    <input type="submit" value="Challenge" style="background-color: green; border-color: darkgreen;">
                    <input type="hidden" name="player" value="ai_player-$#1337"/>
                </form>
            </div>
        <#else>
            <#if numberPlayersOnline-1 == 1>
                <p style = "margin-left: 40px"> There is 1 other player online: </p>
            <#else>
                <p style = "margin-left: 40px"> There are ${numberPlayersOnline-1} other players online: </p>
            </#if>
            <div class="playerListings">
                Challenge AI Player
                <form action="/" method="POST" style="display: inline;float: right;">
                    <input type="submit" value="Challenge" style="background-color: green; border-color: darkgreen;">
                    <input type="hidden" name="player" value="ai_player-$#1337"/>
                </form>
            </div>
            <#list playerList as player>
                <#if player.getName() != currentUser.getName() >
                    <div class="playerListings">
                        ${player.getName()}
                        <form action="/" method="POST" style="display: inline;float: right;">
                            <input type="submit" value="Challenge"  ${player.isInGame()?then('disabled','')}>
                            <input type="hidden" name="player" value="${player.getName()}"/>
                            <input type="hidden" name="mode" value="challenge"/>
                        </form>
                    </div>
                </#if>
            </#list>
        </#if>
    <#else>
        <#if onlyOnePlayer>
            <p style = "margin-left: 40px"> There is ${numberPlayersOnline} player online. </p>
        <#else>
            <p style = "margin-left: 40px"> There are ${numberPlayersOnline} players online. </p>
        </#if>
    </#if>


    <#if playerSignIn && gameList??>
        <br>
        <strong>Active Games</strong><br/>
        <#if gameList?size <= 0>
            <ol>
                There are no active games at this time.
            </ol>
        <#else>
            <#if gameList?size == 1>
                <p style = "margin-left: 40px"> There is 1 active game: </p>
            <#else>
                <p style = "margin-left: 40px"> There are ${gameList?size} active games: </p>
            </#if>

            <#list gameList as game>
                <div class="playerListings">
                    ${game[0]}
                    <form action="/" method="POST" style="display: inline;float: right;">
                        <input type="submit" class="spectateBtn" value="Spectate">
                        <input type="hidden" name="gameID" value="${game[1]}"/>
                        <input type="hidden" name="mode" value="spectate"/>
                    </form>
                </div>
            </#list>
        </#if>
    </#if>


    <!-- TODO: future content on the Home:
            to start games,
            spectating active games,
            or replay archived games
    -->

  </div>

</div>
</body>

</html>
