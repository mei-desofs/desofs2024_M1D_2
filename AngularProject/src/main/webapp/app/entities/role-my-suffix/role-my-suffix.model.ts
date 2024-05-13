export interface IRoleMySuffix {
  id: number;
  nameRole?: string | null;
}

export type NewRoleMySuffix = Omit<IRoleMySuffix, 'id'> & { id: null };
