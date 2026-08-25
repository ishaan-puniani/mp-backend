/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.services.auditLog;

import com.mp.be.models.auditLog.AuditLogRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import com.mp.be.database.entities.AuditLog;
import com.mp.be.database.repositories.AuditLogRepository;
import com.mp.be.services.ServiceOptions;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuditLogServiceImpl implements AuditLogService {
	
	@Autowired
	private AuditLogRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Page<AuditLog> findAndCountAll(ServiceOptions serviceOptions,
										  AuditLogRequestModel requestModel,
										  Optional<Integer> limit,
										  Optional<Integer> offset,
										  Optional <String> orderBy) {

		Criteria criteria = new Criteria();
		criteria.and("tenant").is(serviceOptions.getCurrentTenantId());

		if(requestModel.getFilter()!=null){
			Map<String, Object> filters = requestModel.getFilter();

			filters.forEach((key, value) -> {
				switch (key) {

					case "entityId":
						String entityId = (String) value;
						if(!entityId.isEmpty()) {
							criteria.and("entityId").regex(".*" + entityId + ".*", "i");
						}
						break;
					case "action":
						String action = (String) value;
						if(!action.isEmpty()) {
							criteria.and("action").regex(".*" + action + ".*", "i");
						}
						break;
					case "createdByEmail":
						String createdByEmail = (String) value;
						if(!createdByEmail.isEmpty()) {
							criteria.and("createdByEmail").regex(".*" + createdByEmail + ".*", "i");
						}
						break;
					case "entityNames":
						List<String> entityNames = (List<String>) value;
						if(!entityNames.isEmpty()) {
							criteria.and("email").in(entityNames);
						}
						break;
					case "timestampRange":
						List<String> timestampRange = (List<String>) value;
						if(!timestampRange.isEmpty()) {
							if(timestampRange.get(0) != null) {
								criteria.and("timestamp").gte(timestampRange.get(0));
							}
							if (timestampRange.get(1) != null) {
								criteria.and("timestamp").lte(timestampRange.get(1));
							}
						}
						break;

					default:
						// Handle unknown keys or ignore them
						break;
				}
			});

		}

		int page = offset.orElse(0) / limit.orElse(10);

		Sort sort = orderBy.map(property -> property.isEmpty() ? Sort.unsorted() : Sort.by(Sort.Direction.ASC, property))
				.orElse(Sort.unsorted());
		PageRequest pageRequest = PageRequest.of(page, limit.orElse(10),sort);

		Query query = new Query(criteria);

		// Count the total number of matching documents
		long count = mongoTemplate.count(query, AuditLog.class);

		// Execute the query with pagination
		List<AuditLog> rows = mongoTemplate.find(query.with(pageRequest), AuditLog.class);

		// Create a Page object
		return PageableExecutionUtils.getPage(rows, pageRequest, () -> count);

	}
	
	@Override
	public AuditLog find(ServiceOptions serviceOptions, String id) {
		
		return repository.findById(id).orElse(null);
	}

	@Override
	public AuditLog create(ServiceOptions serviceOptions, AuditLog data) {
	    
	   
	    return repository.save(data);
	}

	@Override
	public void delete(ServiceOptions serviceOptions, String id) {
		
		repository.deleteById(id);
		
	}

}