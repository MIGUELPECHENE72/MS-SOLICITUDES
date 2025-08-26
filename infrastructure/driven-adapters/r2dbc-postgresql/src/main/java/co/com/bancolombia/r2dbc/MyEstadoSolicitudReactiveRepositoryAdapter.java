package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.estadosolicitud.EstadoSolicitud;
import co.com.bancolombia.model.estadosolicitud.gateways.EstadoSolicitudRepository;
import co.com.bancolombia.r2dbc.entity.estadoSolicitud.EstadoSolicitudEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MyEstadoSolicitudReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        EstadoSolicitud/* change for domain model */,
        EstadoSolicitudEntity/* change for adapter model */,
        Integer,
        MyEstadoSolicitudReactiveRepository
> implements EstadoSolicitudRepository {
    public MyEstadoSolicitudReactiveRepositoryAdapter(MyEstadoSolicitudReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, EstadoSolicitud.class/* change for domain model */));
    }

}
