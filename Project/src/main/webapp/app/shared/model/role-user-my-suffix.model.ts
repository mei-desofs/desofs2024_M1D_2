import { IUserMySuffix } from 'app/shared/model/user-my-suffix.model';
import { IRoleMySuffix } from 'app/shared/model/role-my-suffix.model';

export interface IRoleUserMySuffix {
  id?: number;
  userId?: IUserMySuffix | null;
  roleId?: IRoleMySuffix | null;
}

export const defaultValue: Readonly<IRoleUserMySuffix> = {};
