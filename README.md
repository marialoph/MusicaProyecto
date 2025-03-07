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
---

### **VERSION 1.6 Cámara y galería**
En esta versión solo he cambiado cuatro archivos:
Los dialogos `addDialog` y `editDialog`, utilizando los métodos necesarios para permitir y configurar el uso de la cámara y galería.
`addDialog`:
```java
class AddDialog(private val musicaAdd: (Musica) -> Unit) : DialogFragment() {

    lateinit var binding: AddEditDeleteBinding

    // Definir los lanzadores de actividades para la cámara y la galería
    private lateinit var activityResultLauncherCamera: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncherGallery: ActivityResultLauncher<Intent>

    private val RESPUESTA_PERMISO_CAMARA = 100

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AddEditDeleteBinding.inflate(LayoutInflater.from(context))

        // Inicializo los lanzadores de actividad para cámara y galería
        crearLanzadorActividadCamara()
        crearLanzadorActividadGaleria()

        // Configuro el botón de cambio de imagen desde la galería
        binding.buttonGaleria.setOnClickListener {
            cambiarImagenGaleria()
        }

        // Configuro el botón para tomar una foto con la cámara
        binding.buttonCamara.setOnClickListener {
            if (compruebaPermisosCamara()) {
                tomarFotoCamara()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), RESPUESTA_PERMISO_CAMARA)
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Añadir Nuevo Artista")
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = binding.editNombre.text.toString()
                val generoMusical = binding.editGeneroMusical.text.toString()
                val albums = binding.editAlbums.text.toString()
                val fechaNacimiento = binding.editFechaNacimiento.text.toString()
                val imagenUrl = binding.imageView2.tag.toString()

                if (nombre.isNotBlank() && generoMusical.isNotBlank() &&
                    albums.isNotBlank() && fechaNacimiento.isNotBlank() && imagenUrl.isNotBlank()) {
                    val nuevaMusica = Musica(nombre, generoMusical, albums, fechaNacimiento, imagenUrl)
                    musicaAdd(nuevaMusica)
                } else {
                    Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
    }

    // Método para crear el lanzador de la cámara
    private fun crearLanzadorActividadCamara() {
        activityResultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val photoBitmap = result.data?.extras?.get("data") as Bitmap
                val base64Image = convertidorBase64(photoBitmap)

                // Actualiza la vista con Glide
                Glide.with(this)
                    .load("data:image/jpeg;base64,$base64Image")
                    .into(binding.imageView2)  // Actualiza la imagen en el ImageView

                binding.imageView2.tag = "data:image/jpeg;base64,$base64Image"
            }
        }
    }

    // Método para crear el lanzador de la galería
    private fun crearLanzadorActividadGaleria() {
        activityResultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri = result.data?.data

                // Muestra la imagen seleccionada en el ImageView
                Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.degradadocardview)
                    .into(binding.imageView2)

                binding.imageView2.tag = selectedImageUri.toString()
            }
        }
    }

    // Método para verificar si tenemos permisos de cámara
    private fun compruebaPermisosCamara(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    // Método para tomar una foto con la cámara
    private fun tomarFotoCamara() {
        val intentCamara = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activityResultLauncherCamera.launch(intentCamara)
    }

    // Método para cambiar la imagen desde la galería
    private fun cambiarImagenGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncherGallery.launch(intent)
    }

    // Método para convertir un bitmap a una cadena base64
    private fun convertidorBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
```

`EditDialog`:
```java
class EditDialog(
    private val musica: Musica,
    private val musicaEdit: (Musica) -> Unit
) : DialogFragment() {

    private lateinit var binding: AddEditDeleteBinding

    // Definir los lanzadores de actividades para la cámara y la galería
    private lateinit var activityResultLauncherCamera: ActivityResultLauncher<Intent>
    private lateinit var activityResultLauncherGallery: ActivityResultLauncher<Intent>

    private val RESPUESTA_PERMISO_CAMARA = 100

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = AddEditDeleteBinding.inflate(LayoutInflater.from(context))

        // Prellenamos los campos con los datos del artista
        binding.editNombre.setText(musica.nombre)
        binding.editGeneroMusical.setText(musica.generoMusical)
        binding.editAlbums.setText(musica.albums)
        binding.editFechaNacimiento.setText(musica.fechaNacimiento)

        // Aquí verifico si el artista tiene una imagen URL y la mostramos
        if (!musica.image.isNullOrBlank()) {
            Glide.with(this)
                .load(musica.image)
                .into(binding.imageView2)

            binding.imageView2.tag = musica.image
        } else {
            binding.imageView2.setImageResource(R.drawable.degradadocardview)
        }

        // Configuramos el botón de cambio de imagen desde la galería
        binding.buttonGaleria.setOnClickListener {
            cambiarImagenGaleria()
        }

        // Configuramos el botón para tomar una foto con la cámara
        binding.buttonCamara.setOnClickListener {
            if (compruebaPermisosCamara()) {
                tomarFotoCamara()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), RESPUESTA_PERMISO_CAMARA)
            }
        }

        // Inicializamos los lanzadores de actividad para cámara y galería
        crearLanzadorActividadCamara()
        crearLanzadorActividadGaleria()

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Editar Artista")
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = binding.editNombre.text.toString()
                val generoMusical = binding.editGeneroMusical.text.toString()
                val albums = binding.editAlbums.text.toString()
                val fechaNacimiento = binding.editFechaNacimiento.text.toString()
                val imagenUrl =  musica.image

                // Verifico si todos los campos están completos
                if (nombre.isNotBlank() && generoMusical.isNotBlank() &&
                    albums.isNotBlank() && fechaNacimiento.isNotBlank()) {
                    // Creamos el objeto Musica editado con la imagen actualizada
                    val musicaEditada = Musica(nombre, generoMusical, albums, fechaNacimiento, imagenUrl)

                    musicaEdit(musicaEditada)

                    // Se vuelve a actualizar la vista con la imagen nueva
                    if (!musica.image.isNullOrBlank()) {
                        Glide.with(this)
                            .load("data:image/jpeg;base64," + musica.image)
                            .into(binding.imageView2)
                    }
                } else {
                    Toast.makeText(requireContext(), "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }


            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

    }

    // Método para crear el lanzador de la cámara
    private fun crearLanzadorActividadCamara() {
        activityResultLauncherCamera = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val photoBitmap = result.data?.extras?.get("data") as Bitmap
                val base64Image = convertirdorBase64(photoBitmap)

                // Actualiza la vista con Glide
                Glide.with(this)
                    .load("data:image/jpeg;base64,$base64Image")
                    .into(binding.imageView2)  // Actualizo la imagen en el ImageView

                binding.imageView2.tag = "data:image/jpeg;base64,$base64Image"

                saveImageToGallery(photoBitmap)
            }
        }
    }

    //Guardar la imagen en galeria
    private fun saveImageToGallery(bitmap: Bitmap) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val contentResolver = context?.contentResolver
        val uri = contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            contentResolver.openOutputStream(it)?.use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
            context?.contentResolver?.notifyChange(it, null)
        }
    }


    // Método para convertir un bitmap a una cadena base64
    private fun convertirdorBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    // Método para crear el lanzador de la galería
    private fun crearLanzadorActividadGaleria() {
        activityResultLauncherGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri = result.data?.data
                Glide.with(this)
                    .load(selectedImageUri)
                    .placeholder(R.drawable.degradadocardview)
                    .into(binding.imageView2)
                musica.image = selectedImageUri.toString()
            }
        }
    }

    // Método para verificar si tenemos permisos de cámara
    private fun compruebaPermisosCamara(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    // Método para tomar una foto con la cámara
    private fun tomarFotoCamara() {
        val intentCamara = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        activityResultLauncherCamera.launch(intentCamara)
    }

    // Método para manejar los permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RESPUESTA_PERMISO_CAMARA -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tomarFotoCamara()
                } else {
                    Toast.makeText(requireContext(), "No se ha concedido el permiso para usar la cámara", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Método para cambiar la imagen desde la galería
    private fun cambiarImagenGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncherGallery.launch(intent)
    }
}
```
He cambiado el archivo xml de layout he cambiado el cuadro de texto de la imagen a dos botones para la camara y para la galería, y una imagen para actualizar la nueva imagen.
```xml
<Button
        android:id="@+id/buttonCamara"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="44dp"
        android:background="@drawable/botonpulsar"
        android:text="Cámara"
        app:backgroundTint="@null"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.128"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/imageView2"
        tools:ignore="MissingConstraints" />

    <Button
        android:id="@+id/buttonGaleria"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="44dp"
        android:background="@drawable/botonpulsar"
        android:text="Galería"
        app:backgroundTint="@null"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.874"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/imageView2"
        tools:ignore="MissingConstraints" />

    <ImageView
        android:id="@+id/imageView2"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:layout_marginTop="9dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/editFechaNacimiento"
        tools:ignore="MissingConstraints"
        tools:srcCompat="@tools:sample/avatars" />
```

Para que funcione correctamente se añade en el `AndroidManifest`, los permisos necesarios para que funcionen:
```xml
  <uses-permission android:name="android.permission.CAMERA"
        tools:ignore="PermissionImpliesUnsupportedChromeOsHardware" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />

```


---

### **VERSION 4.1: Adaptar el login y el crud a la api **
Una vez hecho el backend con **persistence with JWT**, es decir, una api que cuando registre un nuevo usuario y me logué, me genere un token, y que gracias a ese token me deje ver el listado de datos.
Para hacer la conexión he creado:
- Carpeta `data`:
  - `/network`:
    - `/models`:
      - _AuthResponse_: Representa la respuesta de autenticación que se obtiene al iniciar sesión.
      - _LoginRequest_: Modelo para enviar los datos de login a la Api.
      - _RegisterRequest_ : Modelo para enviar los datos de registro a la Api.
    - `/repository`:
        - _Repository_: Se encarga de manejar todas las operaciones de autenticación y Crud de música a través de la Api.
    - `/service`:
      - _ApiService_: Define las peticiones HTTP necesarias para interactuar con la Api.
  Dentro de la carpeta network tengo el fichero
 _InstanceRetrofit_: Encargado de crear la instancia de Retrofit con la URL base y el manejo del token.

-Carpeta `domain`:
  He añadido dos nuevos casos de uso:
  - _LoginUseCase_: Caso de uso para manejar la lógica de autenticación.
  - _RegisterUseCase_: Caso de uso para manejar el registro de usuarios.

Las clases modificadas para que al conectar con la api funcione ha sido:
  - _MusicaViewModel_: He modificado para mostrar los datos desde la Api.
  - _LoginActivity_: ya que antes utilizaba firebase y he tenido que adaptarlo a la conexión.
  - _RegisterActivity_: ya que antes utilizaba firebase y he tenido que adaptarlo a la conexión.
  - _FragmentMusica_: Adaptado para obtener, agregar, actualizar y eliminar música desde la Api.

---
`AuthResponse`:
```java
@Serializable
data class AuthResponse(
    val token: String,
    val nombre: String,
    val contrasena: String
)
```

`LoginRequest`:
```java
@Serializable
data class LoginRequest(
    val nombre: String,
    val contrasena: String
)

```

`RegisterRequest`:
```java
@Serializable
data class RegisterRequest(
    val nombre: String,
    val contrasena: String
)
```

`Repository`:
```java

class Repository(private val apiService: ApiService, private val context: Context) {

    // Métodos de autenticación
    suspend fun loginUser(nombre: String, contrasena: String): Result<String> {
        return try {
            val response = apiService.loginUser(LoginRequest(nombre, contrasena))
            if (response.isSuccessful) {
                val body = response.body()
                val token = body?.token ?: return Result.failure(Exception("Token no recibido"))
                saveToken(token)
                Result.success(token)
            } else {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Método para registrar un nuevo usuario
    suspend fun registerUser(nombre: String, contrasena: String): Result<String> {
        return try {
            val response = apiService.registerUser(RegisterRequest(nombre, contrasena))
            if (response.isSuccessful) {
                Result.success("Usuario registrado con éxito")
            } else {
                Result.failure(Exception("Error en el registro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Método para obtener todo_ el listado de mucisa
    suspend fun getAllMusica(): Result<List<Musica>> {
        return try {
            val token = getToken()
            if (token.isEmpty()) return Result.failure(Exception("Token no encontrado"))
            val response = apiService.getAllMusica("Bearer $token")
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Método para añadir musica
    suspend fun addMusica(musica: Musica): Result<Unit> {
        return try {
            val token = getToken()
            if (token.isEmpty()) return Result.failure(Exception("Token no encontrado"))
            val response = apiService.addMusica("Bearer $token", musica)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al agregar la música"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Método para actualizar musica
    suspend fun updateMusica(musica: Musica, nombreMusica: String): Result<Unit> {
        return try {
            val token = getToken()
            if (token.isEmpty()) return Result.failure(Exception("Token no encontrado"))
            val response = apiService.updateMusica("Bearer $token", musica, nombreMusica)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al actualizar la música"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //Método para eliminar musica
    suspend fun deleteMusica(nombreMusica: String): Result<Unit> {
        return try {
            val token = getToken()
            if (token.isEmpty()) return Result.failure(Exception("Token no encontrado"))
            val response = apiService.deleteMusica("Bearer $token", nombreMusica)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar la música"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Estos dos métodos para manejar el token
    fun saveToken(token: String) {
        val prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        prefs.edit().putString("TOKEN", token).apply()
    }

    private fun getToken(): String {
        val prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        return prefs.getString("TOKEN", "") ?: ""
    }
}

```
`ApiService`:
```java
interface ApiService {
    @POST("/auth")
    suspend fun loginUser(@Body user: LoginRequest): Response<AuthResponse>

    @POST("/register")
    suspend fun registerUser(@Body user: RegisterRequest): Response<Unit>

    @GET("/musica")
    suspend fun getAllMusica(@Header("Authorization") token: String): List<Musica>

    @POST("/musica")
    suspend fun addMusica(@Header("Authorization") token: String, @Body musica: Musica): Response<Unit>

    @PATCH("/musica/{nombreMusica}")
    suspend fun updateMusica(
        @Header("Authorization") token: String,
        @Body musica: Musica,
        @Path("nombreMusica") nombreMusica: String
    ): Response<Unit>

    @DELETE("/musica/{nombreMusica}")
    suspend fun deleteMusica(@Header("Authorization") token: String, @Path("nombreMusica") nombreMusica: String): Response<Unit>
}

```
`InterfaceRetrofit`:
```java

object InstanceRetrofit {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private fun getClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor { chain ->
                val token = getToken(context)
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
        }.build()
    }

    fun getInstance(context: Context): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private fun getToken(context: Context): String {
        val prefs = context.getSharedPreferences("APP_PREFS", Context.MODE_PRIVATE)
        return prefs.getString("TOKEN", "") ?: ""
    }
}
```


`LoginUseCase`:
```java
class LoginUseCase(private val repository: Repository) {
    suspend fun login(nombre: String, contrasena: String): Result<String> {
        return repository.loginUser(nombre, contrasena)
    }
}
```
`RegisterUseCase`:
```java
class RegisterUseCase(private val repository: Repository) {
    suspend fun register(nombre: String, contrasena: String): Result<String> {
        return repository.registerUser(nombre, contrasena)
    }
}

```
`LoginActivity`:
```java
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginUseCase: LoginUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicaRepository = Repository(InstanceRetrofit.getInstance(this), this)
        loginUseCase = LoginUseCase(musicaRepository)

        binding.btnLoguear.setOnClickListener { loginUser() }
        binding.btnRegistrarLogin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun loginUser() {
        val email = binding.editUsuarioLogin.text.toString().trim()
        val password = binding.editPasswordLogin.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        binding.btnLoguear.isEnabled = false // Deshabilita el botón mientras carga, para evitar que si pincho dos veces se me generen diferentes token

        lifecycleScope.launch {
            val result = loginUseCase.login(email, password)
            result.onSuccess {
                Toast.makeText(this@LoginActivity, "Login exitoso", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }.onFailure { exception ->
                Toast.makeText(
                    this@LoginActivity,
                    exception.message ?: "Error al iniciar sesión",
                    Toast.LENGTH_LONG
                ).show()
            }
            binding.btnLoguear.isEnabled = true
        }
    }
}

```
`RegisterActivity`:
```java
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerUseCase: RegisterUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val musicaRepository = Repository(InstanceRetrofit.getInstance(this), this)
        registerUseCase = RegisterUseCase(musicaRepository)

        binding.btnRegistrar.setOnClickListener { registerUser() }
        binding.btnLogueo.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val email = binding.editUserRegister.text.toString()
        val password = binding.editPasswordRegister.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos", Toast.LENGTH_LONG).show()
            return
        }

        lifecycleScope.launch {
            val result = registerUseCase.register(email, password)
            result.onSuccess {
                Toast.makeText(this@RegisterActivity, it, Toast.LENGTH_LONG).show()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }.onFailure {
                Toast.makeText(this@RegisterActivity, it.message ?: "Error desconocido", Toast.LENGTH_LONG).show()
            }
        }
    }
}

```

`FragmentMusica`:
```java


class FragmentMusica : Fragment() {
    private lateinit var binding: FragmentMusicaBinding
    private lateinit var viewModel: MusicaViewModel
    private lateinit var adapter: AdapterMusica

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicaBinding.inflate(inflater, container, false)
        val context = requireContext()
        val apiService = InstanceRetrofit.getInstance(context)
        val musicaRepository = Repository(apiService, context)
        viewModel = MusicaViewModel(musicaRepository)

        adapter = AdapterMusica(mutableListOf(), { musica ->
            val pos = adapter.listaMusica.indexOf(musica)
            if (pos != -1) {
                showDeleteDialog(musica,pos)
            }
        }, { musica ->
                showEditDialog(musica)
        })

        binding.myRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.myRecyclerView.adapter = adapter

        binding.buttonAnnadir.setOnClickListener {
            showAddDialog()
        }


        viewModel.musicaListData.observe(viewLifecycleOwner) { lista ->
            if (lista.isNotEmpty()) {
                adapter.listaMusica = lista.toMutableList()  // Actualizo la lista en el Adapter
                adapter.notifyDataSetChanged()  // Notifico al Adapter de los cambios
                binding.myRecyclerView.visibility = View.VISIBLE
            } else {
                binding.myRecyclerView.visibility = View.GONE
            }
        }

        // Cargo la lista de música desde el backend
        // Llamo a la función que obtiene los datos de la API
        viewModel.showMusica()
        return binding.root
    }

    //Método para mostrar el diálogo añadir
    private fun showAddDialog() {
        val dialog = AddDialog { nuevaMusica ->
            viewModel.addMusica(nuevaMusica)
        }
        dialog.show(childFragmentManager, "AddDialog")    }

    //Método para mostrar el diálogo editar
    private fun showEditDialog(musica: Musica) {
        val dialog = EditDialog(musica) { updatedMusica ->
            viewModel.updateMusica(updatedMusica)
        }
        dialog.show(childFragmentManager, "EditDialog")
    }

    // Método para mostrar el diálogo de eliminar
    private fun showDeleteDialog(musica: Musica, pos: Int) {
        val dialog = DeleteDialog(musica) {
            viewModel.deleteMusica(pos)
        }
        dialog.show(childFragmentManager, "DeleteDialog")
    }
}
```
Para que funcione es importante añadir en el `AndroidManifest`:
Para permitir el tráfico HTTP.
```xml
android:usesCleartextTraffic="true"
```

**EN CONCLUSIÓN:**

He realizado un cambio completo de Firebase a una API con autenticación JWT. Ahora, los usuarios pueden registrarse, autenticarse y gestionar música utilizando mi propio backend.
