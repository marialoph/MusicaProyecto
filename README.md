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
---

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

---

### **VERSION 1.3 Autenticación y creación de usuario en Firebase**
**Para añadir Firebase al proyecto**
1. Me creo una cuenta en _Firebase_
2. Creo un proyecto en _Firebase_
3. Registro mi app Android (Añadiendo el nombre del paquete de mi app) en el proyecto creado en el punto dos.
4. Agrego la configuración necesaria de _Firebase_ en mi app.
   - Descargo el json google-services.json y lo llevo a la raíz del proyecto.
   - Luego agrego las dependencias necesarias en el gradle.
```
    gradle(Module:app)

    //plugins
    id("com.google.gms.google-services")

    //dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.auth.ktx)
```

```
    gradle(Proyect:app)
    id("com.google.gms.google-services") version "4.4.2" apply false

```


He añadido dos nuevos ficheros:
- `LoginActivity`: Maneja el inicio de sesión de un usuario mediante Firebase Authentication.
- `RegisterActivity`: Gestiona el registro de  nuevos usuarios utilizando Firebase Authentication.

En el layout se crea el diseño tanto del register como del login:
- `activity_login.xml`:
   - ImageView
   - EditText email
   - EditText contraseña
   - Button iniciar sesión
   - Button registrarse
   - Button olvidé contraseña
- `activity_register.xml`:
    - ImageView
    - EditText email
    - EditText contraseña
    - EditText repetir contraseña
    - Button registrarse
    - Button iniciar sesión

---
 
### **VERSION 1.4 Añado Navigation Drawer**
Convierto el activity donde tengo el listado de recyclerView en Fragment.
El fragment `FragmentMusica` contiene:
```kotlin
class FragmentMusica : Fragment() {
    lateinit var binding: FragmentMusicaBinding
    lateinit var controller: Controller
    lateinit var activitycontext : MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        activitycontext = requireActivity() as MainActivity
        binding = FragmentMusicaBinding.inflate(inflater, container, false)
        controller = Controller(activitycontext, this)
        initRecyclerView()

        binding.buttonAnnadir.setOnClickListener {
            controller.mostrarAddDialog()
        }

        return binding.root
    }

    // Este método configura el RecyclerView
    private fun initRecyclerView() {
        binding.myRecyclerView.layoutManager = LinearLayoutManager(context)
        controller.setAdapter()
    }

}
```
Creo dos fragmentos más: `FragmentHome` y `FragmentSetting`, para tener más opciones en el menu.
El `MainActivity` he añadido dos variables nuevas:
```kotlin
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
```
Sirve para configurar el menú y que sea visible mediante los siguientes métodos:
```kotlin
override fun onSupportNavigateUp(): Boolean{
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }


    //Navegación del menú de opciones.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragmentHome -> {
                navController.navigate(R.id.fragmentHome)
                updateTitulo("Inicio")
                true
            }

            R.id.fragmentSetting -> {
                navController.navigate(R.id.fragmentSetting)
                updateTitulo("Setting")
                true
            }

            R.id.fragmentLogout -> {
                logout()
                true
            }

            R.id.fragmentMusica -> {
                navController.navigate(R.id.fragmentMusica)
                updateTitulo("Artistas")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
```
Para el diseño de ambos menús he añadido y modificado algunos ficheros:
- `Layout`:
  - _activity_main.xml_ (Se modifica para mostrar ambos menús)
  - _app_bar_layout.xml_ (Para el toolbar)
  - _content.xml_
  - _fragment_home.xml_
  - _fragment_musica.xml_
  - _fragment_setting_
  - _nav_header.xml_ (Para el drawer)
- `Menu`:
  - _nav_menu.xml_ (Para el drawer)
  - _toolbar.xml_
- `Navigation`:
  - _nav_graph_ (Para la navegación de ambos menus)

 ---

### **VERSION 1.5 Adaptación del proyecto a mvvm**
He realizado una reestructuración completa del proyecto a la arquitectura MVVM(Model_View_ViewModel).

He creado tres carpetas principales `data`, `domain` y `ui`, dentro de cada carpeta encontramos:

---

- Carpeta `data`: Manejo de datos.
   - `datasource`: **Repository** contiene la fuente de datos.
   - `repository`: **MusicaRepository** administra la lista de objetos(musica) y gestiona su almacenamiento.
     
---

- Carpeta `domain`: Se encarga de manejar la lógica.
  - `models`: He creado **ListMusica** para almacenar la lista y **Musica** es el modelo de datos para representar el artista.
  - `repository`: He creado **InterfaceDao** define los métodos que se implementaran para su buen funcionamiento.
  - `usecase`: He creado los casos de uso **AllMusicaUseCase**, **DeleteMusicaUseCase**, **NewMusicaUseCase** y **UpdateMusicaUseCase** que encapsulan una lógica específica.

---

- Carpeta `ui` : Contiene las partes de la interfaz de usuario.
  - `adapter`: **AdapterMusica** y **ViewHMusica** para mostrar y manejar los elementos de la lista.
  - `viewmodel`: He creado `MusicaViewModel` como intermedio entre la interfaz de usuario y la lógica.
  - `views`: 
    - `activities`: **LoginActivity**, **MainActivity** y **RegisterActivity**
    - `dialogues`: **AddDialog**, **DeleteDialog** y **EditDialog**
    - `fragment`: **FragmentHome**, **FragmentSetting** y **FragmentMusica**(lo modifico para adaptarlo a la nueva estructura)

---

Ficheros nuevos que he creado:

_MusicaRepository_
```java
class MusicaRepository() : InterfaceDao {
    var musicaList: MutableList<Musica> = mutableListOf()

    //Método que devuelve la lista de musica
    override fun getDataMusica(): MutableList<Musica> {
        return Repository.listaMusica.toMutableList()
    }

    //Método que elimina un objeto(musica) de la lista por su posición
    override suspend fun deleteMusica(pos: Int): Boolean {
        return if (pos >= 0 && pos < musicaList.size) {
            musicaList.removeAt(pos)
            true
        } else {
            false
        }
    }

    //Método que añade al repositorio un objeto(musica) nuevo
    override suspend fun addMusica(musica: Musica) : Musica?{
        ListMusica.music.musica.add(musica)
        return musica
    }

    //Método que actualiza un objeto(musica) en la posicion en la que esta
    override suspend fun update(pos: Int, musica: Musica): Boolean {
        if (pos >= 0 && pos < musicaList.size) {
            musicaList[pos] = musica
            return true
        }
        return false
    }

    //Método que verifica si el objeto(musica) existe
    override suspend fun exisMusica(musica: Musica) : Boolean = ListMusica.music.musica.contains(musica)


    //Devuelve el objeto(musica) en una posición específica
    override fun getMusicaByPos (pos:Int) : Musica? {
        return if (pos < ListMusica.music.musica.size)
            ListMusica.music.musica.get(pos)
        else
            null
    }
}
```

_ListMusica_
```java
class ListMusica private constructor(){
    var musica: MutableList<Musica> = mutableListOf()

    companion object{
        val music : ListMusica by lazy {
            ListMusica()
        }
    }
}
```
_InterfaceDao_
```java
interface InterfaceDao {
    fun getDataMusica(): List<Musica>

    suspend fun deleteMusica(id:Int) : Boolean

    suspend fun addMusica(musica: Musica) : Musica?

    suspend fun update(id: Int, musica: Musica) : Boolean

    suspend fun exisMusica(musica: Musica) : Boolean

    fun getMusicaByPos(pos:Int) : Musica?
}
```

_AllMusicaUseCase_
```java
class AllMusicaUseCase (private  val musicaRepository: MusicaRepository) {
     operator fun invoke(): MutableList<Musica> {
        return musicaRepository.getDataMusica()
    }
}
```

_DeleteMusicaUseCase_
```java
class DeleteMusicaUseCase(private val musicaRepository: MusicaRepository) {
    suspend operator fun invoke(pos: Int): Boolean {
        return musicaRepository.deleteMusica(pos)
    }
}
```

_NewMusicaUseCase_
```java
class NewMusicaUseCase(private val musicaRepository: MusicaRepository) {
    suspend operator fun  invoke(newMusica: Musica) : Musica?{
        return if (!musicaRepository.exisMusica(newMusica)){
            return musicaRepository.addMusica(newMusica)
        }else{
            null
        }
    }
}
```

_UpdateMusicaUseCase_
```java
class UpdateMusicaUseCase(private val musicaRepository: MusicaRepository) {
    suspend operator fun invoke(pos: Int, musica: Musica): Boolean {
        return musicaRepository.update(pos, musica)
    }
}
```

_MusicaViewModel_
```java
class MusicaViewModel () : ViewModel() {
    //Inicializo el repositorio y los casos de uso
    private val musicaRepository = MusicaRepository()
    private val getAllMusicaUseCase = AllMusicaUseCase(musicaRepository)
    private val newMusicaUseCase = NewMusicaUseCase(musicaRepository)
    private val updateMusicaUseCase = UpdateMusicaUseCase(musicaRepository)
    private val deleteMusicaUseCase = DeleteMusicaUseCase(musicaRepository)
    val musicaListData = MutableLiveData<List<Musica>>()


    //Método para obtener y mostrar toda la lista de musica
    fun showMusica() {
        viewModelScope.launch {
            val data: List<Musica> = getAllMusicaUseCase()
            musicaListData.postValue(data)
        }
    }


    //Método que añade una nueva música
    //Llamo al caso de uso para agregar la nueva musica
    //Por último actualizo la lista
    fun addMusica(musica: Musica) {
        viewModelScope.launch {
            val newMusica = newMusicaUseCase(musica)
            newMusica?.let {
                val updatedList = musicaListData.value?.toMutableList() ?: mutableListOf()
                updatedList.add(it)
                musicaListData.postValue(updatedList)
            }
        }
    }

    //Método para actualizar una música de la lista
    //Si la posición es válida se actualiza la música en esa posición.
    //Se actualiza la lista
    fun updateMusica(musica: Musica, pos: Int) {
        viewModelScope.launch {
            val updatedList = musicaListData.value?.toMutableList() ?: mutableListOf()

            if (pos >= 0 && pos < updatedList.size) {
                updatedList[pos] = musica
                musicaListData.postValue(updatedList)
                updateMusicaUseCase(pos, musica)
            }
        }
    }


    //Método para eliminar una musica de la lista
    //Si la posición es válida se elimina la música en esa posición
    //Se actualiza la lista
    fun deleteMusica(pos: Int) {
        viewModelScope.launch {
            val updatedList = musicaListData.value?.toMutableList() ?: mutableListOf()
            if (pos >= 0 && pos < updatedList.size) {
                updatedList.removeAt(pos)
                musicaListData.postValue(updatedList)
                deleteMusicaUseCase(pos)
            }
        }
    }


}
```

_FragmentMusica_ (Lo modifico para adaptarlo a la nueva estructura)
```java
class FragmentMusica : Fragment() {

    lateinit var binding: FragmentMusicaBinding
    lateinit var activitycontext: MainActivity
    lateinit var adapterMusica: AdapterMusica
    val musicaRepository = MusicaRepository()
    private val musicaViewModel: MusicaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMusicaBinding.inflate(inflater, container, false)
        return binding.root
    }

    //Se configura el recyclerview, el adapter y se observa los cambios en los datos
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.myRecyclerView.layoutManager = LinearLayoutManager(activity)
        setAdapter(mutableListOf())
        setObserver() // Observa los cambios en los datos
        musicaViewModel.showMusica() // Muestra todos los datos

        //Configuro el boton para mostrar el dialogo añadir
        binding.buttonAnnadir.setOnClickListener {
            btnAddOnClickListener()
        }
    }

    //Método que muestra el dialogo y lo actualiza
    private fun btnAddOnClickListener() {
        val dialog = AddDialog() { musica ->
            musicaViewModel.addMusica(musica)
        }
        dialog.show(requireActivity().supportFragmentManager, "Añadir un nuevo artista")
    }

    //Método que muestra el listado de musica
    private fun setAdapter(musics: MutableList<Musica>) {
        adapterMusica = AdapterMusica(
            musics,
            { musica -> delMusica(musica) },
            { musica -> updateMusica(musica) }
        )
        binding.myRecyclerView.adapter = adapterMusica
    }

    //Muestra el dialogo para editar
    //Se obtiene el indice de la musica a editar
    //Se actualiza
    private fun updateMusica(musica: Musica) {
        val editDialog = EditDialog(musica) { editMusica ->
            // Obtiene el índice de la música a actualizar
            val pos = musicaViewModel.musicaListData.value?.indexOfFirst { it.nombre == musica.nombre } ?: -1
            if (pos != -1) {
                musicaViewModel.updateMusica(editMusica, pos)
            } else {
                Log.e("Edit", "Música no encontrada para editar.")
            }
        }
        editDialog.show(requireActivity().supportFragmentManager, "Editar un artista")
    }


    //Muestra el diálogo para eliminar
    //Se obtiene el indice de la musica a eliminar
    //Se actualiza
    private fun delMusica(musica: Musica) {
        val dialog = DeleteDialog(musica.nombre) {
            // Obtiene el índice de la música a eliminar
            val pos = musicaViewModel.musicaListData.value?.indexOfFirst { it.nombre == musica.nombre } ?: -1
            if (pos != -1) {
                musicaViewModel.deleteMusica(pos)
            } else {
                Log.e("Delete", "Música no encontrada para eliminar.")
            }
        }
        dialog.show(requireActivity().supportFragmentManager, "Eliminar un artista")
    }

    //Se observan los cambios de la lista
    private fun setObserver() {
        musicaViewModel.musicaListData.observe(viewLifecycleOwner) { musics ->
            setAdapter(musics.toMutableList())
        }
    }

}
```




