Interface zum Senden von Mails.

Implementierung: Anders als in eKFSG würd ich empfehlen die Mails asynchron zu senden, d.h. anstatt sie direkt zu
senden: zuerst in der DB speichern und dann senden: So haben wir diese Vorteile:

* Können retries machen (vielleicht 2-3 retries; dann nicht mehr neu versuchen).
* Haben auch in der DB was geloggt (ist auch gut zum manuellen Testing; man kann in der DB nachschauen, welche Mails
  generiert wurden).
