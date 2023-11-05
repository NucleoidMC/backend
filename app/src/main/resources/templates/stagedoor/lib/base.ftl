<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Nucleoid Stagedoor</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css">
        <!-- Tiny Potato says trans rights! 🏳️‍⚧️ -->
    </head>
    <body>
        <#nested>

        <div class="flex"></div>

        <footer class="footer">
            <div class="content has-text-centered">
                <a href="https://nucleoid.xyz">NucleoidMC</a>
                Stagedoor - part of
                <a href="https://github.com/NucleoidMC/nucleoid-backend-java">nucleoid-backend</a>.
            </div>
        </footer>
    </body>
    </html>
</#macro>

<#macro navbar>
    <nav class="navbar" role="navigation" aria-label="main navigation">
        <div class="navbar-brand">
            <#-- TODO: add nucleoid logo -->
<#--            <a class="navbar-item" href="/">-->
<#--                <img src="/assets/icons/icon.png" alt="icon" width="28">-->
<#--            </a>-->
        </div>
        <a role="button" class="navbar-burger" aria-label="menu" aria-expanded="false" id="menu-button">
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
            <span aria-hidden="true"></span>
        </a>

        <div id="navbarMain" class="navbar-menu">
            <a class="navbar-item" href="/stagedoor/">
                Home
            </a>

            <a class="navbar-item" href="/stagedoor/logout">
                Sign out
            </a>
        </div>

        <script>
            document.getElementById("menu-button").onclick = function() {
                document.getElementById("menu-button").classList.toggle('is-active');
                document.getElementById("navbarMain").classList.toggle('is-active');
            }
        </script>
    </nav>
</#macro>
