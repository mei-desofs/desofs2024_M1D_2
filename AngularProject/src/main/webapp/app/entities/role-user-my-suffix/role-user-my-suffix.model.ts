import { IUserMySuffix } from 'app/entities/user-my-suffix/user-my-suffix.model';
import { IRoleMySuffix } from 'app/entities/role-my-suffix/role-my-suffix.model';

export interface IRoleUserMySuffix {
  id: number;
  userId?: Pick<IUserMySuffix, 'id'> | null;
  roleId?: Pick<IRoleMySuffix, 'id'> | null;
}

export type NewRoleUserMySuffix = Omit<IRoleUserMySuffix, 'id'> & { id: null };
