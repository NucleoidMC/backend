<#import "lib/base.ftl" as u>

<@u.page>
    <@u.navbar></@u.navbar>
    <section class="section">
        <main class="container">
            <div class="content">
                <h1>Logout</h1>
                <p>
                    Are you sure you want to log out?
                </p>
                <form method="post">
                    <div class="field">
                        <p class="control">
                            <input type="submit" class="button is-danger" value="Log out">
                        </p>
                    </div>
                </form>
            </div>
        </main>
    </section>
</@u.page>
