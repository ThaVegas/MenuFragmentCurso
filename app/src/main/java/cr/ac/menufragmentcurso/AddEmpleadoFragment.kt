package cr.ac.menufragmentcurso

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.repository.EmpleadoRepository
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var empleado: Empleado? = null
    lateinit var img_avatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            empleado = it.get(ARG_PARAM1) as Empleado?
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view: View = inflater.inflate(R.layout.fragment_add_empleado, container, false)
        val textNombre = view.findViewById<TextView>(R.id.addNombre)
        val textIdem = view.findViewById<TextView>(R.id.addidEmpleado)
        val textCedula = view.findViewById<TextView>(R.id.addCedula)
        val textPuesto = view.findViewById<TextView>(R.id.addPuesto)
        val textDepartamento = view.findViewById<TextView>(R.id.addDepartamento)
        img_avatar = view.findViewById(R.id.avatar)

        if (empleado?.avatar != "") {

            img_avatar.setImageBitmap(empleado?.avatar?.let { decodeImage(it) })
        }


        view.findViewById<Button>(R.id.addConfirmar).setOnClickListener() {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("??Desea agregar el usuario?")
                .setCancelable(false)
                .setPositiveButton("S??") { dialog, id ->
                    var idEmpleado: Int = EmpleadoRepository.instance.datos().size + 1
                    var avatar:String=encodeImage(img_avatar.drawable.toBitmap()).toString()

                    //--------------------
                    var empleado : Empleado = Empleado (idEmpleado,
                        textCedula?.text.toString(),
                        textNombre?.text.toString(),
                        textPuesto?.text.toString(),
                        textDepartamento?.text.toString(), avatar)
                    //---------------
                    EmpleadoRepository.instance.save(empleado)

                    val fragmento: Fragment = fragment_Camera()
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                }
                .setNegativeButton(
                    "No"
                ) { dialog, id ->
                    // logica del no
                }
            val alert = builder.create()
            alert.show()

        }


        view.findViewById<Button>(R.id.addCancelar).setOnClickListener() {
            val fragmento: Fragment = fragment_Camera()
            fragmentManager
                ?.beginTransaction()
                ?.replace(R.id.home_content, fragmento)
                ?.commit()
        }
        img_avatar.setOnClickListener {
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE )
        }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            var imageUri = data?.data

            Picasso.get()
                .load(imageUri)
                .resize(120, 120)
                .centerCrop()
                .into(img_avatar)
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT).replace("\n", "")
    }

    private fun decodeImage(b64: String): Bitmap {
        val imageBytes = Base64.decode(b64, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param empleado Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(empleado: Empleado) =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {


                    putSerializable(ARG_PARAM1, empleado)
                }
            }
    }
}