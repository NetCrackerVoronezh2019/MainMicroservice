package com.mainmicroservice.mainmicroservice.Services;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.*;

import com.mainmicroservice.mainmicroservice.ElasticRepositorys.UserElasticSearchRepository;
import com.mainmicroservice.mainmicroservice.Entities.User;

import Models.Enums.TeacherStatus;

@Service
public class UserElasticSearchService {

	@Autowired
	private UserElasticSearchRepository userESRepository;
	
	
	@Autowired 
	private RoleService roleService;
	
	public List<User> filterTeachers(String text)
	{
		Long roleId=this.roleService.findRoleByRoleName("ROLE_TEACHER").getRoleId();
		QueryBuilder query =QueryBuilders
    			.boolQuery()
    			.must(QueryBuilders.matchQuery("role.roleId", roleId))
    			.must(QueryBuilders.multiMatchQuery(text)
				.field("firstname")
				.field("lastname")
				.fuzziness(Fuzziness.TWO)
				  .prefixLength(3)
				.type(MultiMatchQueryBuilder.Type.BEST_FIELDS));
    		
    			
    	

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();
        return this.userESRepository.search(build).toList();
	}
	
	
	public List<User> filterNonCheckedTeacher(String text)
	{
		Long roleId=this.roleService.findRoleByRoleName("ROLE_TEACHER").getRoleId();
		
		QueryBuilder query =QueryBuilders
    			.boolQuery()
    			.must(QueryBuilders.matchQuery("role.roleId", roleId))
    			.must(QueryBuilders.matchQuery("teacherStatus",TeacherStatus.CERTIFICATES_ARE_NOT_CHECKED.toString().toLowerCase()))
    			.must(QueryBuilders.multiMatchQuery(text)
				.field("firstname")
				.field("lastname")
				.fuzziness(Fuzziness.TWO)
				  .prefixLength(3)
				.type(MultiMatchQueryBuilder.Type.BEST_FIELDS));
    		
    			
    	

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();
        return this.userESRepository.search(build).toList();
	}
	
	
	public List<User> filterCheckedTeacher(String text)
	{
		
			
		Long roleId=this.roleService.findRoleByRoleName("ROLE_TEACHER").getRoleId();
		System.out.println(roleId);
		List<String> statuses=new ArrayList<>();
		statuses.add(TeacherStatus.CERTIFIED_SPECIALIST.toString().toLowerCase());
		statuses.add(TeacherStatus.NOT_A_CERTIFIED_SPECIALIST.toString().toLowerCase());
		statuses.add(TeacherStatus.EMPTY.toString().toLowerCase());
		
		
		
		QueryBuilder query =QueryBuilders
    			.boolQuery()
    			.must(QueryBuilders.matchQuery("role.roleId",roleId))
    			.must(QueryBuilders.termsQuery("teacherStatus",statuses))
    			.must(QueryBuilders.multiMatchQuery(text)
				.field("firstname")
				.field("lastname")
				.fuzziness(Fuzziness.TWO)
				  .prefixLength(3)
				.type(MultiMatchQueryBuilder.Type.BEST_FIELDS));
    		
    			
    	

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();
        return this.userESRepository.search(build).toList();
	}
	
	
	public List<User> filterAll(String text)
	{
		QueryBuilder query =QueryBuilders
    			.boolQuery()
    			
    			.must(QueryBuilders.multiMatchQuery(text)
				.field("firstname")
				.field("lastname")
				.type(MultiMatchQueryBuilder.Type.BEST_FIELDS));
    		
    			
    	

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(query)
                .build();
        return this.userESRepository.search(build).toList();
	}
}
