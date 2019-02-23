# Jira Telegram Bot

[![DepShield Badge](https://depshield.sonatype.org/badges/owner/repository/depshield.svg)](https://depshield.github.io)

Jira-telegram-bot is a [Spring Boot](https://github.com/spring-projects/spring-boot) application which handing 
[Jira](https://www.atlassian.com/software/jira) webhook events and sends notifications via 
[Telegram](https://telegram.org) bot.

## Key features

- processing Jira webhook issue events
- notification templating (using [Apache FreeMarker](https://freemarker.apache.org) template engine by default)
- support several databases (PostgreSQL, MySQL, H2)


## Getting started 

### Build

You can build application using following command:

    ./gradlew clean build
    
#### Requirements:

JDK >= 1.8

### Running jira-telegram-bot

After the build you get [fully executable jar archive](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/#packaging-executable-configuring-launch-script)
 
You can run application using following commands:

    java -jar jira-telegram-bot
or

    ./jira-telegram-bot

### Configuration

You can see all the necessary configuration properties in the file [example/application.properties](examples/application.properties)

According to [Spring Docs](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files)
you can override default application properties by put custom **application.properties** file in one of the following
locations:

- a `/config` subdirectory of the current directory
- the current directory

#### Custom properties

<dl>
  <dt>jira.bot.template.type</dt>
  <dd>type of template processing engine (default FREEMARKER)</dd>
  
  <dt>jira.bot.notification.sendToMe</dt>
  <dd>is need to notify user about self-created events</dd>
    
  <dt>jira.bot.notification.jiraUrl</dt>
  <dd>jira instance url for building browse link in notification message</dd>
  
  <dt>telegram.bot.token</dt>
  <dd>telegram bot secret token</dd>
  
  <dt>telegram.bot.name</dt>
  <dd>telegram bot name</dd>
  
  <dt>telegram.bot.proxyHost</dt>
  <dd>http proxy host</dd>

  <dt>telegram.bot.proxyPort</dt>
  <dd>http proxy port</dd>
</dl>


### Jira Webhooks

To receive jira webhooks you may to configure your jira instance. [See jira docs](https://developer.atlassian.com/server/jira/platform/webhooks/)

**WARNING!** 
> Jira-telegram-bot supports only **issue** events at the moment

By default jira-telegram-bot process only following issue events:

- issue_created
- issue_updated
- issue_generic
- issue_commented

If you want to process any other issue event or change default template you can modify corresponding row in jira-telegram-bot
database table called **templates**.

### Templating

Jira-telegram-bot using [Apache FreeMarker](https://freemarker.apache.org) template engine by default. All templates by default
stored in jira-telegram-bot database table called **templates**.
Each template must be a message in properly [telegram markdown style](https://core.telegram.org/bots/api#markdown-style).

In [example/templates](examples/templates) folder you can find default jira event templates.

If you want to add another templating logic, you can implement **TemplateService** interface.

### Jira user registration

To register jira user to receive webhook events you should add corresponding row into jira-telegram-bot database table called **chats**.

You should specify **jira_id** (jira user login) and **telegram_id** (telegram chat id) unique fields.

To find out your telegram chat id you should write simple command "/me" to telegram bot.

### Telegram bot commands

Telegram bot supports following text commands:

- /me - prints telegram chat id
- /jira_login - prints attached jira login to this telegram chat id 
- /help - prints help message
