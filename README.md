# DBGAPI
A simple wrapper for Discord Bots Group api

# Posting server count
If you use [JDA](https://github.com/DV8FromTheWorld/JDA) I really recommend just doing:
```Java
new DBGAPI(jda, token); // jda is your JDA instance and token is the String of your api token
```
it will automatically post server count for you!

otherwise you can use
```Java
DBGAPI api = new DBGAPI(token, botid); // token is a String of your api token, botid is a String of your bot's id
api.postServerCount(servercount); // servercount is a long of your server count
```
and you can use
```Java
DBGAPI.postServerCount(jda, token); // jda is your JDA instance and token is the String of your api token
```
and lastly 
```Java
DBGAPI.postServerCount(servercount, token, botid); // jda is your JDA instance and token is the String of your api token, botid is a String of your bot's id
```

# Getting bot information
```Java
JSONObject info = DBGAPI.getBotInfo(botid); // botid is a String of the bot's id
```
