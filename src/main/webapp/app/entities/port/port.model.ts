export interface IPort {
  id?: number;
  code?: string | null;
  name?: string | null;
}

export class Port implements IPort {
  constructor(public id?: number, public code?: string | null, public name?: string | null) {}
}

export function getPortIdentifier(port: IPort): number | undefined {
  return port.id;
}
