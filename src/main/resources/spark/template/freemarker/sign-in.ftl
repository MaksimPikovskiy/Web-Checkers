<!DOCTYPE html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
  <title>Web Checkers | ${title}</title>
  <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

  <h1>Web Checkers | ${title}</h1>

  <!-- Provide a navigation bar -->
  <#include "nav-bar.ftl" />

  <div class="body">

    <form action="./signin" method="POST">
      <!-- Provide a message to the user, if supplied. -->
      <#include "message.ftl" />
      <label for="uname">Username:</label>
      <input type= "text" id="uname" name="uname" autofocus>

      <br/><br/>
      <button type="submit">signin</button>
    </form>
    <br/><br/>

    <!-- <label for="password">Password:</label>
    <input type= "text" id="password" name="password"> -->

  </div>

</div>
</body>

</html>
