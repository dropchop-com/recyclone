package com.dropchop.recyclone.base.es.model.query;

import com.dropchop.recyclone.base.api.model.invoke.ErrorCode;
import com.dropchop.recyclone.base.api.model.invoke.ServiceException;
import com.dropchop.recyclone.base.api.model.query.KnnCondition;
import com.dropchop.recyclone.base.api.model.query.condition.Knn;
import lombok.Getter;

@Getter
@SuppressWarnings("unused")
public class KnnNodeObject extends QueryNodeObject {
  private final String field;
  private final float[] queryVector;
  private final String queryString;
  private final Integer k;
  private final Float similarity;
  private final Integer numCandidates;
  private final QueryNodeObject filter;
  private final QueryNodeObject self = new QueryNodeObject();

  private void validateAndBuild() {
    if (field == null || field.isEmpty()) {
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

    this.self.put("field", field);

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
      if (filter instanceof BoolQueryObject boolQuery) {
        this.self.put("filter", boolQuery);
      } else {
        this.self.put("filter", filter);
      }
    }

    this.put("knn", self);
  }

  public KnnNodeObject(IQueryNode parent, Knn knnQuery, QueryNodeObject filterQuery) {
    super(parent);
    this.field = knnQuery.get$knn().getName();
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

  public void replaceQueryVector(float[] queryVector) {
    if (queryVector == null || queryVector.length == 0) {
      this.self.remove("query_vector");
    } else {
      this.self.put("query_vector", queryVector);
    }
  }

  public void removeQueryVector() {
    replaceQueryVector(null);
  }

  public void renameField(String field) {
    this.put("field", field);
  }

  public void replaceSimilarity(Float similarity) {
    if (similarity == null) {
      this.self.remove("similarity");
      return;
    }
    this.self.put("similarity", similarity);
  }

  public void replaceTopK(Integer topK) {
    if (topK == null) {
      this.self.remove("k");
      return;
    }
    this.self.put("k", topK);
  }

  public void replaceNumCandidates(Integer numCandidates) {
    if (numCandidates == null) {
      this.self.remove("num_candidates");
      return;
    }
    this.self.put("num_candidates", numCandidates);
  }
}
