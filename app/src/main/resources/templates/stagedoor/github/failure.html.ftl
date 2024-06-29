<#-- @ftlvariable name="message" type="String" -->
<#import "../lib/base.ftl" as u>

<@u.page>
    <section class="section">
        <main class="container">
            <div class="content">
                <h1>
                    GitHub login failed
                </h1>
                <p>
                    Sorry, you aren't able to log in right now.
                </p>
                <#if message != "generic">
                <p>
                    <#if message == "not_admin">
                        You are not an administrator.
                    <#elseif message = "not_member">
                        You are not a member.
                    </#if>
                </p>
                </#if>
            </div>
        </main>
    </section>
</@u.page>
