package cr.ac.menufragmentcurso.service

import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.entity.Record
import retrofit2.Call
import retrofit2.http.*

interface EmpleadoService {

 @GET("empleado")
 fun getEmpleado(): Call<Record>

 @PUT("empleado/{IdEmpleado}")
 fun update(@Path("IdEmpleado") IdEmpleado: Int, @Body empleado: Empleado): Call<String>

 //@Delete empleado/{idEmpleado}
 @DELETE("empleado/{idEmpleado}")
 fun delete (@Path("idEmpleado") idEmpleado: Int): Call<String>

 //@Create
 @POST("empleado")
 fun post(@Body empleado: Empleado): Call<String>


}