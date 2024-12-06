# Documentación Proyecto Música
### _María López Hernández-Carrillo_

#### Descripción del proyecto
Aplicación sobre diferentes artistas y géneros musicales.
En el inicio habrá un listado de cardview con los artistas más escuchados.

### **VERSION 1.1 Utilización del RecyclerView y posibilidad de borrar**
**Estructura del proyecto por packages:**
- `adapter`:
  - AdapterMusica
  - ViewHMusica
- `controller`:
  - Controller
- `dao`:
  - DaoMusica
- `interfaces`:
  - InterfaceMusica
- `models`:
  - Musica
- `objects_models`:
  - Repository
- MainActivity

**De que se encarga cada clase:**
- _AdapterMusica_:  Se encarga de crear la vista para cada ítem, y de asignar los datos a cada uno.
- _ViewHMusica_:  Se encarga de mostrar la información de cada objeto Musica(nombre, generoMusical, albums, fechaNacimiento, image), además configura el botón de eliminar.
- _Controller_:  Se encarga de gestionar la lógica del activity y maneja la eliminación de un item de la lista pulsando el boton borrar, mostrando un mensaje con el nombre del artista eliminado.
- _DaoMusica_:  Implementa de la interfaz InterfaceMusica, se encarga de obtener los datos de música desde una lista (Repository.listaMusica). Gracias al método getDataMusica(), se podrá acceder a los datos de la lista.
- _InterfaceMusica_:  Es una interfaz con un solo método, que es implementada por DaoMusica para la obtención de los datos de la lista.
- _Musica_:  Esta clase representa a un artista musical y contiene atributos. También se sobrescribe el método toString.
- _Repository_:  Es un objeto que almacena una lista estática de objetos Musica.
- _MainActivity_:  Es el activity principal. Se configura el adaptador para el RecyclerView mediante el método setAdapter del controller. También otro método para poner la disposición de los elementos en una lista vertical.

**Estructura de las vistas:**
- `drawable`:
  - degradadocardview.xml
  - degradadocolor.xml
  - papelera.png
- `layout`:
  - activity_main.xml
  - item_musica.xml

**Diseño de las vistas:**

Para el degradado del fondo del activity principal (degradadocolor):
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android"
    tools:ignore="ExtraText">
    <gradient
        android:startColor="#4D2F6F"
        android:endColor="#ABC8F4"
        android:centerColor="#1EE595"
        android:angle="45" />
</shape>
```

Para el degradado del fondo de los cardview (degradadocolor):
```kotlin
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android"
    tools:ignore="ExtraText">
    <gradient
        android:startColor="#2E8D30"
        android:endColor="#1DC1D6"
        android:centerColor="#466EB6"
        android:angle="45" />
</shape>
```
_activity_main_: Está compuesto por un FrameLayout. Tiene un RecyclerView que se usa para mostrar la lista con todos sus atributos.

_item_musica_: Se usa un cardview para mostrar la informacion de un artista.

**Modificaciones en el gradle:**

Añadimos la configuración del binding e implementamos la librería glide para cargar las imagenes.
```kotlin
 viewBinding {
        enable = true
    }
```
```kotlin
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.github.bumptech.glide:glide:4.15.1") //LIBRERIA
}
```

**Modificación en el fichero AndroidManifest:**

Damos el permiso de internet para que las imágenes se puedan ver.
```xml
<uses-permission android:name="android.permission.INTERNET" />
```


### **VERSION 1.2 CRUD completo con alta, edición y borrado en memoria**
**Cambios en las clases ya creadas de la versión anterior**
- _AdapterMusica_: Se ha añadido una nueva variable `onEditClick`.
- _ViewHMusica_: Se ha añadido el botón editar en el método `setOnClickListener`.
- _Controller_: Se han añadido los metodos para mostrar los dialogos y actualizarlos (add, edit, delete).
  
  Los métodos añadidos son los siguientes:
  ```java
  
  //             AÑADIR
  
  //Muestra la vista del AddDialog
    fun mostrarAddDialog(){
        val addDialog = AddDialog {
            musica -> actualizaMusicaNueva(musica)
        }
        val activity = context as AppCompatActivity
        addDialog.show(activity.supportFragmentManager, "AddDialog")

    }
    //Actualiza el cardiewnuevo
    private fun actualizaMusicaNueva(musica: Musica) {
        listaMusica.add(musica)
        (binding.myRecyclerView.adapter as AdapterMusica).notifyItemInserted(listaMusica.size - 1)
        Toast.makeText(context,"Nuevo artista ${musica.nombre}", Toast.LENGTH_SHORT).show()
    }

  

  //            EDITAR
  
  //Muestra la vista del EditDialog para editar algun campo del artista seleccionado
    fun mostrarEditDialog(musica: Musica) {
        val editDialog = EditDialog(musica) {
            musicaEditada ->
            actualizaMusicaEditada(musicaEditada)
        }
        val activity = context as AppCompatActivity
        editDialog.show(activity.supportFragmentManager, "EditDialog")
    }

    //Actualiza el dato que se ha modificado
    fun actualizaMusicaEditada(musica: Musica) {
        val position = listaMusica.indexOfFirst {
            it.nombre == musica.nombre
        }
        if (position != -1) {
            listaMusica[position] = musica
            (binding.myRecyclerView.adapter as AdapterMusica).notifyItemChanged(position)
            Toast.makeText(context,"El artista ${musica.nombre} se ha modificado", Toast.LENGTH_SHORT).show()

        }
    }


  //         DELETE
  
  // Muestra el dialogo y actualiza al pulsar el boton borrar.
    fun delMusica(pos: Int) {
        val nombreArtista = listaMusica[pos].nombre
        val dialog = DeleteDialog(nombreArtista) {
            listaMusica.removeAt(pos)
            (binding.myRecyclerView.adapter as AdapterMusica).apply {
                notifyItemRemoved(pos)
                notifyItemRangeChanged(pos, listaMusica.size - pos)
            }
            Toast.makeText(context, "Se eliminó el artista: $nombreArtista", Toast.LENGTH_SHORT).show()
        }
        dialog.show((context as AppCompatActivity).supportFragmentManager, "DeleteDialog")
    }
  

  ```
  **Lo que he creado nuevo**
  
  Un package llamado `dialogues` que contiene los siguientes ficheros:
  - _AddDialog_
  - _DeleteDialog_
  - _EditDialog_
    
  Para que al pulsar los botones se muestren los dialogos.
