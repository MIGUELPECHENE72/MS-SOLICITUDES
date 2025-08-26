package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.tipoSolicitud.TipoSolicitudEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// TODO: This file is just an example, you should delete or modify it
public interface MyTipoSolicitudReactiveRepository extends ReactiveCrudRepository<TipoSolicitudEntity, Integer>, ReactiveQueryByExampleExecutor<TipoSolicitudEntity> {

}
