package com.dropchop.recyclone.base.es.model.query.cond;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.KnnCondition;
import com.dropchop.recyclone.base.es.model.query.IQueryNode;
import com.dropchop.recyclone.base.es.model.query.IQueryObject;
import com.dropchop.recyclone.base.es.model.query.QueryObject;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class Knn extends QueryObject {
  private String fieldName;
  private float[] queryVector;
  private Integer k;
  private Float similarity;
  private Integer numCandidates;

  private final String queryString;
  private final IQueryObject filter;
  private final IQueryObject body = new QueryObject();

  private void validateAndBuild() {
    if (fieldName == null || fieldName.isEmpty()) {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "kNN query must have a field specified"
      );
    }

    if (queryVector != null && queryVector.length > 0) {
      this.body.put("query_vector", queryVector);
    } else if (queryString != null && !queryString.isBlank()) {
      this.body.put("query_vector", new float[]{});
    } else {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "kNN query must have query_vector or query_string specified"
      );
    }

    this.body.put("field", fieldName);

    if (k != null) {
      this.body.put("k", k);
    }

    if (similarity != null) {
      this.body.put("similarity", similarity);
    }

    if (numCandidates != null) {
      this.body.put("num_candidates", numCandidates);
    }

    if (filter != null) {
      if (filter instanceof Bool boolQuery) {
        this.body.put("filter", boolQuery);
      } else {
        this.body.put("filter", filter);
      }
    }

    this.put("knn", body);
  }

  public Knn(IQueryNode parent, com.dropchop.recyclone.base.api.model.query.condition.Knn knnQuery,
             IQueryObject filterQuery) {
    super(parent);
    this.fieldName = knnQuery.get$knn().getName();
    KnnCondition<?> condition = knnQuery.get$knn();
    Object value = condition.getValue();
    if (value instanceof float[] floats) {
      this.queryVector = floats;
      this.queryString = null;
    } else if (value instanceof String str) {
      this.queryVector = null;
      this.queryString = str;
    } else {
      this.queryVector = null;
      this.queryString = null;
    }
    this.k = condition.getTopK();
    this.filter = filterQuery;
    if (filterQuery != null) {
      this.filter.setParent(this);
    }
    this.similarity = condition.getSimilarity();
    this.numCandidates = condition.getNumCandidates();

    validateAndBuild();
  }

  public void setQueryVector(float[] queryVector) {
    if (queryVector == null || queryVector.length == 0) {
      this.body.remove("query_vector");
      this.queryVector = null;
    } else {
      this.body.put("query_vector", queryVector);
      this.queryVector = queryVector;
    }
  }

  public void removeQueryVector() {
    setQueryVector(null);
  }

  public void setFieldName(String field) {
    this.fieldName = field;
    this.body.put("field", field);
  }

  public void setSimilarity(Float similarity) {
    if (similarity == null) {
      this.body.remove("similarity");
      this.similarity = null;
    } else {
      this.body.put("similarity", similarity);
      this.similarity = similarity;
    }
  }

  public void setTopK(Integer topK) {
    if (topK == null) {
      this.body.remove("k");
      this.k = null;
    } else {
      this.body.put("k", topK);
      this.k = topK;
    }
  }

  public void setNumCandidates(Integer numCandidates) {
    if (numCandidates == null) {
      this.body.remove("num_candidates");
      this.numCandidates = null;
    } else {
      this.body.put("num_candidates", numCandidates);
      this.numCandidates = numCandidates;
    }
  }
}
