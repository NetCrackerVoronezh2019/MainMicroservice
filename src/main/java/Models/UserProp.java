package Models;

import java.time.LocalDateTime;
import Models.Enums.BlockType;

public class UserProp {

	public Long userId;
	public String firstname;
	public String lastname;
	public boolean isActivate;
	public boolean isDeleted;
	public BlockType blockType;
	public String role;
	public String email;
	
	
	public LocalDateTime getBanCancelDate(BlockType _isBlocked)
	{
		if(_isBlocked==BlockType.NONE)
			return null;
		if(_isBlocked==BlockType.ONE_DAY)
			return LocalDateTime.now().plusDays(1);
		if(_isBlocked==BlockType.TOW_DAYS)
			return LocalDateTime.now().plusDays(2);
		if(_isBlocked==BlockType.ONE_MONTH)
			return LocalDateTime.now().plusMonths(1);
		if(_isBlocked==BlockType.ONE_YEAR)
			return LocalDateTime.now().plusYears(1);
		return null;
		
	}
}
