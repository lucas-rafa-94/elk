package com.waterfy.elk.repository;

import com.waterfy.elk.model.ElkQueryModel;
import com.waterfy.elk.model.enums.BancoDeDadosEnum;
import com.waterfy.elk.model.enums.TipoUploadEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ElkQueryRepository extends CrudRepository<ElkQueryModel, Long> {
    ElkQueryModel findById(long id);
    List<ElkQueryModel> findAll();
    List<ElkQueryModel> findByDatabase(BancoDeDadosEnum bancoDeDadosEnum);
    List<ElkQueryModel> findByTipoUpload(TipoUploadEnum tipoUploadEnum);
    @Transactional
    void deleteById(long id);
}
