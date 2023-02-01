#Struktur

Die Struktur des Frameworks ist in verschieden Bestandteile aufgeteilt.


### Manifests

In dieser XML werden allgemeine Parameter festgelegt. Zb welche berechtigungen benötigt werden.
In dem Fall dieser APP wären es berechtigungen das Internet und die Kamera zu benutzten
bzw. den Benutzer zu fragen ob sie verwendet werden dürfen.

Des Weiteren wird in dieser Datei festgelegt wo die App startet. In unserem Fall wäre es die
LoginActivity.

## Java

In diesem Ordner ist die komplette Buisness Logik der App zu finden. 

### ui

Ähnlich wie HTML und JS besitzte jede APP eine visuelle komponenten (XML-Fragments) und
eine logische Komponente (die Java-Klassen).
Im Ui Ordner existieren 2 Activitys. Dies sind die grundlegenden Bausteine für jede App.
Eine Activity kann meherer Fragments "hosten". Der Austausch von Fragemnts ist vergleichbar
als würde man von einer Webpage auf die nächste weitergeleitet werden.
In dieser App wurden der Login vorgang und die eigentliche Anwendung in seperate Acitvities 
ausgelagtert. Dies hat den Grund das die MainActivity erst ausgeführt werden soll sobald der User
eingeloggt ist. Somit kann sichergestellt werden das Anfragen an das Backend nur von Usern 
mit einem Authentifizierten Account erstellt und gesendet werden.

In den unter Ordnern des UI-Ordners werden die einzlenen Fragments und die Logik definiert.
Hier wird zb eingestellt was ein bestimmter Button macht oder was passiert wenn Fragemnt-XYZ
geladen wird.

## res

Wie bereits erwähnt teilt sich eine APP in visuelle und eine logische Komponente. Die visuelle
Komponente wird in res-Ordener in Form von XML-Dateien definiert. 

### layout

Hier sind die Layouts der einzelnen XML-Elemente (Fragments und Activities) zu finden.
Ähnlich wie bei HTML werden Elemente in eine XML-Datei hinzugefügt und bestimmte Attribute
zugewiesen.

### menu
Hier werden die Layouts der Menüs (in unserem Fall zb des Menüs am "Boden" der App)
definiert.

### navigation
Hier wird efiniert welches Element aus navigatorischer Sicht zusammenhängt. Dabei kann beispielsweise
ein Fragment X mit einem Fragement Y verbunden werden. Diese Verbindung wird das Fragment_X_to_Y
gennannt und kann als eigenständiges Objekt innerhalb des Programmcodes genutzt werden.

