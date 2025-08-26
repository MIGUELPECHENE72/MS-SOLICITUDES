package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.tiposolicitud.TipoSolicitud;
import co.com.bancolombia.model.tiposolicitud.gateways.TipoSolicitudRepository;
import co.com.bancolombia.r2dbc.entity.tipoSolicitud.TipoSolicitudEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class MyTipoSolicitudReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        TipoSolicitud/* change for domain model */,
        TipoSolicitudEntity/* change for adapter model */,
        Integer,
        MyTipoSolicitudReactiveRepository
> implements TipoSolicitudRepository {
    public MyTipoSolicitudReactiveRepositoryAdapter(MyTipoSolicitudReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, TipoSolicitud.class/* change for domain model */));
    }

}
