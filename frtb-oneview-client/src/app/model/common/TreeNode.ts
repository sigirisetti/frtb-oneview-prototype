export class TreeNode<T> {
    id: string;
    label: string;
    payload: T;
    children: TreeNode<T>[];
}