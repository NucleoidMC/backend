<#macro page>
    <!DOCTYPE html>
    <html lang="en" class="sl-theme-dark">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Nucleoid Stagedoor</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.11.2/cdn/themes/dark.css" />
        <script type="module" src="https://cdn.jsdelivr.net/npm/@shoelace-style/shoelace@2.11.2/cdn/shoelace.js" ></script>
        <!-- Tiny Potato says trans rights! 🏳️‍⚧️ -->
    </head>
    <body>
        <#nested>

        <div class="flex"></div>

        <footer class="footer">
            <div class="content has-text-centered">
                <a href="https://nucleoid.xyz">NucleoidMC</a>
                Stagedoor - part of
                <a href="https://github.com/NucleoidMC/backend">nucleoid-backend</a>.
            </div>
        </footer>
    </body>
    </html>
</#macro>

<#macro navbar>
    <nav>
        <sl-button-group label="navigation">
            <sl-button href="/stagedoor/">
                Home
            </sl-button>

            <sl-button href="/stagedoor/logout">
                Sign out
            </sl-button>
        </sl-button-group>
    </nav>
</#macro>
