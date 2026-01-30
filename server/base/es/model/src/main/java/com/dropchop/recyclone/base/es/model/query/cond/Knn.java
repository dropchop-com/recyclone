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
  private final IQueryObject self = new QueryObject();

  private void validateAndBuild() {
    if (fieldName == null || fieldName.isEmpty()) {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "kNN query must have a field specified"
      );
    }

    if (queryVector != null && queryVector.length > 0) {
      this.self.put("query_vector", queryVector);
    } else if (queryString != null && !queryString.isBlank()) {
      this.self.put("query_vector", new float[]{});
    } else {
      throw new ServiceException(
          ErrorCode.parameter_validation_error,
          "kNN query must have query_vector or query_string specified"
      );
    }

    this.self.put("field", fieldName);

    if (k != null) {
      this.self.put("k", k);
    }

    if (similarity != null) {
      this.self.put("similarity", similarity);
    }

    if (numCandidates != null) {
      this.self.put("num_candidates", numCandidates);
    }

    if (filter != null) {
      if (filter instanceof Bool boolQuery) {
        this.self.put("filter", boolQuery);
      } else {
        this.self.put("filter", filter);
      }
    }

    this.put("knn", self);
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
      this.self.remove("query_vector");
      this.queryVector = null;
    } else {
      this.self.put("query_vector", queryVector);
      this.queryVector = queryVector;
    }
  }

  public void removeQueryVector() {
    setQueryVector(null);
  }

  public void setFieldName(String field) {
    this.fieldName = field;
    this.self.put("field", field);
  }

  public void setSimilarity(Float similarity) {
    if (similarity == null) {
      this.self.remove("similarity");
      this.similarity = null;
    } else {
      this.self.put("similarity", similarity);
      this.similarity = similarity;
    }
  }

  public void setTopK(Integer topK) {
    if (topK == null) {
      this.self.remove("k");
      this.k = null;
    } else {
      this.self.put("k", topK);
      this.k = topK;
    }
  }

  public void setNumCandidates(Integer numCandidates) {
    if (numCandidates == null) {
      this.self.remove("num_candidates");
      this.numCandidates = null;
    } else {
      this.self.put("num_candidates", numCandidates);
      this.numCandidates = numCandidates;
    }
  }
}
